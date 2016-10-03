package com.alapshin.arctor.annotation;


import com.google.auto.value.AutoValue;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Code generator for ViewStateCommand
 *
 * For every method in view interface annotated with {@link GenerateCommand} generate new class
 * Generated class is public, abstract and annotated with {@link AutoValue} annotation for immutability
 * Every parameter of annotated method becomes public abstract field of generated class
 * Also generated class will have public static `create` method accepting same parameters as annotated method
 */
public class CommandGenerator {
    private final Filer filer;
    private final Messager messager;

    private String packageName;

    public CommandGenerator(Filer filer, Messager messager) {
        this.filer = filer;
        this.messager = messager;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void generateCode(ExecutableElement methodElement)
            throws IOException {
        // Get annotated method name
        String methodName = methodElement.getSimpleName().toString();
        // Get method annotation
        GenerateCommand annotation = methodElement.getAnnotation(GenerateCommand.class);

        // Receive all method parameters
        List<? extends VariableElement> arguments = methodElement.getParameters();
        // List to store types of method parameters
        List<TypeName> argumentTypeNames = new ArrayList<>();
        // List to store generated abstract methods for every parameter
        List<MethodSpec> argumentMethodSpecs = new ArrayList<>();
        List<ParameterSpec> argumentParameterSpecs = new ArrayList<>();
        // For each method parameter create appropriate abstract method in generated class
        for (VariableElement argument : arguments) {
            String argumentName = argument.getSimpleName().toString();
            TypeName argumentTypeName = TypeName.get(argument.asType());
            argumentTypeNames.add(argumentTypeName);

            argumentMethodSpecs.add(MethodSpec.methodBuilder(argumentName)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(argumentTypeName)
                    .build());
            argumentParameterSpecs.add(ParameterSpec.builder(argumentTypeName, argumentName).build());
        }

        // Generate name used for ViewStateCommand using annotated method name
        String commandClassName = String.format("%sCommand",
                CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, methodName));
        TypeName viewTypeName = TypeName.get(methodElement.getEnclosingElement().asType());

        MethodSpec typeMethodSpec = MethodSpec.methodBuilder("type")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.INT)
                .addStatement("return $L", annotation.value())
                .build();

        TypeName commandTypeName = ClassName.get(packageName, commandClassName);

        ParameterSpec viewParameterSpec = ParameterSpec.builder(viewTypeName, "view")
                .build();
        List<String> foobar = new ArrayList<>();
        for (MethodSpec spec : argumentMethodSpecs) {
            foobar.add(spec.name + "()");
        }
        MethodSpec executeMethodSpec = MethodSpec.methodBuilder("execute")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(viewParameterSpec)
                .addStatement("$N.$L($L)", viewParameterSpec.name, methodName, String.join(", ", foobar))
                .build();

        List<String> names = new ArrayList<>();
        for (ParameterSpec spec : argumentParameterSpecs) {
            names.add(spec.name);
        }
        MethodSpec staticCreateMethodSpec = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameters(argumentParameterSpecs)
                .returns(commandTypeName)
                .addStatement("return new AutoValue_$L($L)", commandClassName, String.join(", ", names))
                .build();

        ClassName viewStateCommand = ClassName.get("com.alapshin.arctor.viewstate", "ViewStateCommand");
        TypeName parametrizedViewStateCommand = ParameterizedTypeName.get(viewStateCommand, viewTypeName);
        TypeSpec typeSpec = TypeSpec.classBuilder(commandClassName)
                .addAnnotation(AutoValue.class)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addSuperinterface(parametrizedViewStateCommand)
                .addMethod(typeMethodSpec)
                .addMethods(argumentMethodSpecs)
                .addMethod(executeMethodSpec)
                .addMethod(staticCreateMethodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();
        javaFile.writeTo(filer);
    }
}
