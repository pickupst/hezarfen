/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.plugins.quality;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.gradle.api.Action;
import org.gradle.api.Incubating;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.ClosureBackedAction;
import org.gradle.api.internal.project.IsolatedAntBuilder;
import org.gradle.api.plugins.quality.internal.PmdInvoker;
import org.gradle.api.plugins.quality.internal.PmdReportsImpl;
import org.gradle.api.reporting.Reporting;
import org.gradle.api.resources.TextResource;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.VerificationTask;
import org.gradle.internal.nativeintegration.console.ConsoleDetector;
import org.gradle.internal.nativeintegration.console.ConsoleMetaData;
import org.gradle.internal.nativeintegration.services.NativeServices;
import org.gradle.internal.reflect.Instantiator;

import javax.inject.Inject;
import java.util.List;

/**
 * Runs a set of static code analysis rules on Java source code files and generates a report of problems found.
 *
 * @see PmdPlugin
 * @see PmdExtension
 */
public class Pmd extends SourceTask implements VerificationTask, Reporting<PmdReports> {

    private FileCollection pmdClasspath;
    private List<String> ruleSets;
    private TargetJdk targetJdk;
    private TextResource ruleSetConfig;
    private FileCollection ruleSetFiles;
    private final PmdReports reports;
    private boolean ignoreFailures;
    private int rulePriority;
    private boolean consoleOutput;
    private FileCollection classpath;


    public Pmd() {
        reports = getInstantiator().newInstance(PmdReportsImpl.class, this);
    }


    /**
     * Sets the rule priority threshold.
     */
    @Incubating
    public void setRulePriority(int intValue) {
        validate(intValue);
        rulePriority = intValue;

    }

    @Inject
    public Instantiator getInstantiator() {
        throw new UnsupportedOperationException();
    }

    @Inject
    public IsolatedAntBuilder getAntBuilder() {
        throw new UnsupportedOperationException();
    }

    @TaskAction
    public void run() {
        PmdInvoker.invoke(this);
    }

    public boolean stdOutIsAttachedToTerminal() {
        ConsoleDetector consoleDetector = NativeServices.getInstance().get(ConsoleDetector.class);
        ConsoleMetaData consoleMetaData = consoleDetector.getConsole();
        return consoleMetaData != null && consoleMetaData.isStdOut();
    }

    /**
     * Configures the reports to be generated by this task.
     */
    public PmdReports reports(@DelegatesTo(value = PmdReports.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        return reports(new ClosureBackedAction<PmdReports>(closure));
    }

    /**
     * Configures the reports to be generated by this task.
     *
     * @since 3.0
     */
    public PmdReports reports(Action<? super PmdReports> configureAction) {
        configureAction.execute(reports);
        return reports;
    }

    /**
     * Validates the value is a valid PMD RulePriority (1-5)
     *
     * @param value rule priority threshold
     */
    public static void validate(int value) {
        if (value > 5 || value < 1) {
            throw new InvalidUserDataException(String.format("Invalid rulePriority '%d'.  Valid range 1 (highest) to 5 (lowest).", value));
        }

    }

    /**
     * The class path containing the PMD library to be used.
     */
    @InputFiles
    public FileCollection getPmdClasspath() {
        return pmdClasspath;
    }

    public void setPmdClasspath(FileCollection pmdClasspath) {
        this.pmdClasspath = pmdClasspath;
    }

    /**
     * The built-in rule sets to be used. See the <a href="http://pmd.sourceforge.net/rules/index.html">official list</a> of built-in rule sets.
     *
     * Example: ruleSets = ["basic", "braces"]
     */
    @Input
    public List<String> getRuleSets() {
        return ruleSets;
    }

    public void setRuleSets(List<String> ruleSets) {
        this.ruleSets = ruleSets;
    }

    /**
     * The target JDK to use with PMD.
     */
    @Input
    public TargetJdk getTargetJdk() {
        return targetJdk;
    }

    public void setTargetJdk(TargetJdk targetJdk) {
        this.targetJdk = targetJdk;
    }

    /**
     * The custom rule set to be used (if any). Replaces {@code ruleSetFiles}, except that it does not currently support multiple rule sets.
     *
     * See the <a href="http://pmd.sourceforge.net/howtomakearuleset.html">official documentation</a> for how to author a rule set.
     *
     * Example: ruleSetConfig = resources.text.fromFile(resources.file("config/pmd/myRuleSets.xml"))
     *
     * @since 2.2
     */
    @Incubating
    @Nested
    @Optional
    public TextResource getRuleSetConfig() {
        return ruleSetConfig;
    }

    public void setRuleSetConfig(TextResource ruleSetConfig) {
        this.ruleSetConfig = ruleSetConfig;
    }

    /**
     * The custom rule set files to be used. See the <a href="http://pmd.sourceforge.net/howtomakearuleset.html">official documentation</a> for how to author a rule set file.
     *
     * Example: ruleSetFiles = files("config/pmd/myRuleSets.xml")
     */
    @InputFiles
    public FileCollection getRuleSetFiles() {
        return ruleSetFiles;
    }

    public void setRuleSetFiles(FileCollection ruleSetFiles) {
        this.ruleSetFiles = ruleSetFiles;
    }

    /**
     * The reports to be generated by this task.
     */
    @Nested
    public final PmdReports getReports() {
        return reports;
    }

    /**
     * Whether or not to allow the build to continue if there are warnings.
     *
     * Example: ignoreFailures = true
     */
    @Input
    public boolean getIgnoreFailures() {
        return ignoreFailures;
    }


    public void setIgnoreFailures(boolean ignoreFailures) {
        this.ignoreFailures = ignoreFailures;
    }

    /**
     * Specifies the rule priority threshold.
     *
     * @see PmdExtension#rulePriority
     */
    @Input
    @Incubating
    public int getRulePriority() {
        return rulePriority;
    }

    /**
     * Whether or not to write PMD results to {@code System.out}.
     */
    @Input
    @Incubating
    public boolean isConsoleOutput() {
        return consoleOutput;
    }

    public void setConsoleOutput(boolean consoleOutput) {
        this.consoleOutput = consoleOutput;
    }

    /**
     * Compile class path for the classes to be analyzed.
     *
     * The classes on this class path are used during analysis but aren't analyzed themselves.
     *
     * This is only well supported for PMD 5.2.1 or better.
     */
    @InputFiles
    @Optional
    @Incubating
    public FileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }

}
