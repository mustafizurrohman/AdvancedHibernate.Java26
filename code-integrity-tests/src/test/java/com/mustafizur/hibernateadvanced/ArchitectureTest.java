package com.mustafizur.hibernateadvanced;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.mustafizur.hibernateadvanced", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {
    @ArchTest static final ArchRule domain_is_framework_free = noClasses().that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "jakarta.persistence..", "org.hibernate..");

    @ArchTest static final ArchRule core_is_framework_free = noClasses().that().resideInAPackage("..core..")
        .should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "jakarta..", "org.hibernate..");

    @ArchTest static final ArchRule application_does_not_depend_on_infrastructure = noClasses().that().resideInAPackage("..application..")
        .should().dependOnClassesThat().resideInAPackage("..infrastructure..");
}
