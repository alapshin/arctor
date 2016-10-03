package com.alapshin.arctor.annotation;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ArctorProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    private Types  typesUtil;
    private Elements elementUtil;

    private CommandGenerator commandGenerator;

    public ArctorProcessor() {
        super();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
        this.typesUtil = processingEnv.getTypeUtils();
        this.elementUtil = processingEnv.getElementUtils();

        this.commandGenerator = new CommandGenerator(filer, messager);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(GenerateCommand.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        note("It works");
        for (Element el : roundEnvironment.getElementsAnnotatedWith(GenerateCommand.class)) {
            if (el.getKind() != ElementKind.METHOD) {
                error("Only methods can be annotated with @%s", GenerateCommand.class.getSimpleName());
                return true;
            }
            String packageName = getPackageName(el);
            commandGenerator.setPackageName(packageName);
            try {
                commandGenerator.generateCode((ExecutableElement) el);
            } catch (IOException e) {
                error(el, e.getMessage());
            }
        }

        return true;
    }

    private void note(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(message, args));
    }

    private void note(Element element, String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(message, args), element);
    }

    private void error(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
    }

    private void error(Element element, String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element);
    }

    private String getPackageName(Element element) {
        return elementUtil.getPackageOf(element).getQualifiedName().toString();
    }
}
