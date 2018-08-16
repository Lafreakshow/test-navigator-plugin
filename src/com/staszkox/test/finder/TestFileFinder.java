package com.staszkox.test.finder;

import com.intellij.psi.PsiClass;
import com.staszkox.test.configuration.TestFileSuffix;

public class TestFileFinder extends FileFinder
{
    private TestFileFinder(PsiClass sourceClass)
    {
        super(sourceClass);
    }

    public static TestFileFinder forClass(PsiClass sourceClass)
    {
        return new TestFileFinder(sourceClass);
    }

    @Override
    protected String getFileNameForSearch()
    {
        return getBaseClass().getQualifiedName() + TestFileSuffix.TEST_SUFFIX;
    }
}
