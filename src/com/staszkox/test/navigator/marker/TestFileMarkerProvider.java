package com.staszkox.test.navigator.marker;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.staszkox.test.navigator.configuration.TestFileSuffix;
import com.staszkox.test.navigator.finder.SourceFileFinder;
import com.staszkox.test.navigator.finder.TestFileFinder;
import com.staszkox.test.navigator.icons.NavigationIconsBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public class TestFileMarkerProvider extends RelatedItemLineMarkerProvider
{
    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result)
    {
        if (element instanceof PsiIdentifier && element.getParent() instanceof PsiClass)
        {
            PsiClass clazz = (PsiClass) element.getParent();

            if (isSourceClass(clazz))
            {
                Optional<PsiClass> testFile = TestFileFinder.forClass(clazz).findFile();

                if (testFile.isPresent())
                {
                    if (hasTestCases(testFile.get()))
                    {
                        NavigationGutterIconBuilder<PsiElement> builder =
                                NavigationIconsBuilder.TEST_CLASS_AVAILABLE.target(testFile.get());

                        result.add(builder.createLineMarkerInfo(element));
                    }
                    else
                    {
                        NavigationGutterIconBuilder<PsiElement> builder =
                                NavigationIconsBuilder.TEST_CLASS_HAS_NO_TESTS.target(testFile.get());

                        result.add(builder.createLineMarkerInfo(element));
                    }
                }
                else
                {
                    NavigationGutterIconBuilder<PsiElement> builder =
                            NavigationIconsBuilder.TEST_CLASS_NOT_AVAILABLE.target(element);

                    result.add(builder.createLineMarkerInfo(element));
                }
            }
            else if (isTestClass(clazz))
            {
                Optional<PsiClass> sourceFile = SourceFileFinder.forClass(clazz).findFile();

                if (sourceFile.isPresent())
                {
                    NavigationGutterIconBuilder<PsiElement> builder =
                            NavigationIconsBuilder.SOURCE_CLASS_AVAILABLE.target(sourceFile.get());

                    result.add(builder.createLineMarkerInfo(element));
                }
                else
                {
                    NavigationGutterIconBuilder<PsiElement> builder =
                            NavigationIconsBuilder.SOURCE_CLASS_NOT_AVAILABLE.target(element);

                    result.add(builder.createLineMarkerInfo(element));
                }
            }
        }
    }

    private boolean isSourceClass(PsiClass clazz)
    {
        //TODO validation for inner classes etc.

        if (clazz != null && clazz.getQualifiedName() != null)
        {
            return !clazz.getQualifiedName().endsWith(TestFileSuffix.TEST_SUFFIX);
        }

        return false;
    }

    private boolean isTestClass(PsiClass clazz)
    {
        if (clazz != null && clazz.getQualifiedName() != null)
        {
            return clazz.getQualifiedName().endsWith(TestFileSuffix.TEST_SUFFIX);
        }

        return false;
    }

    private boolean hasTestCases(PsiClass clazz)
    {
        //TODO check if class contains test cases
        return true;
//        return Arrays.stream(clazz.getMethods())
//                .anyMatch(method -> AnnotationUtil.findAnnotation(method, true, "Test") != null);
    }
}
