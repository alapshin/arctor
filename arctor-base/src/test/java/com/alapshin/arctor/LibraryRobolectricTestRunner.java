package com.alapshin.arctor;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;

/**
 * A Robolectric test runner for library projects compatible with Android Gradle plugin 2.2.0-alpha6 and later
 * See <a href="https://gist.github.com/venator85/282df3677af9ecac56e5e4b91471cd8f">
 *     https://gist.github.com/venator85/282df3677af9ecac56e5e4b91471cd8f
 *     </a>
 */
public class LibraryRobolectricTestRunner extends RobolectricTestRunner {
    public LibraryRobolectricTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        AndroidManifest appManifest = super.getAppManifest(config);
        FsFile androidManifestFile = appManifest.getAndroidManifestFile();

        if (androidManifestFile.exists()) {
            return appManifest;
        } else {
            androidManifestFile = FileFsFile.from(getModuleRootPath(config),
                    appManifest.getAndroidManifestFile().getPath().replace("manifests/full", "manifests/aapt"));
            return new AndroidManifest(androidManifestFile, appManifest.getResDirectory(),
                    appManifest.getAssetsDirectory());
        }
    }

    private String getModuleRootPath(Config config) {
        String moduleRoot = config.constants().getResource("").toString().replace("file:", "");
        return moduleRoot.substring(0, moduleRoot.indexOf("/build"));
    }
}
