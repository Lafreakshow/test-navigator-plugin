package com.staszkox.test.navigator.files.finders;

import com.intellij.psi.PsiClass;
import com.staszkox.test.navigator.configuration.TestFileSuffix;

public class SourceFileFinder extends FileFinder
{
    private SourceFileFinder(PsiClass testClass)
    {
        super(testClass);
    }

    public static SourceFileFinder forClass(PsiClass testClass)
    {
        return new SourceFileFinder(testClass);
    }

    @Override
    protected String getFileNameForSearch()
    {
        return getBaseClass().getQualifiedName().replace(TestFileSuffix.TEST_SUFFIX, "");
    }
}