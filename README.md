How to create Intellij IDEA Language Plugin for your DSL?
=========================================================

**1. Download Eclipse plugins and add them into plugins directory of your eclipse.**

There are two Eclipse plugins:
* Intellij IDEA SDK (org.eclipse.xtext.idea.sdk_1.0.0.jar): https://github.com/akosyakov/intellij-idea-sdk
* Intellij IDEA plugin generator (org.eclipse.xtext.generator.idea_1.0.0.jar): https://github.com/akosyakov/intellij-idea-plugin-generator

You can check out the whole project using git or download the last project version as zip via github UI.

**2. Create or open xText project, define grammar, launch generator and export plugins.**

**3. Add the Intellij IDEA language plugin generator plugin as dependency to your xText project.**

1. Open META-INF/MANIFEST.MF -> Dependencies -> Add... -> Select org.eclipse.xtext.generator.idea -> OK -> Save manifest

**4. Edit Language Generation workflow.**

There are two generator fragments to add:
* PsiAntlrGeneratorFragment generates Antlr grammar file and then uses Antlr tool to generate a parser and a lexer which are adapted to work with PsiBuilder.
* IdeaPluginGenerator generates Intellij IDEA Language Plugin project.

Look configuration example and read comments carefully:
```
//Added before workflow declaration
var pluginsPath = "c:/eclipse-SDK-4.2-Xtext-2.3.1-win32/plugins"
...
//Added at the end of Generator
component = Generator {
...
	language = {
	...
		fragment = idea.PsiAntlrGeneratorFragment {}

		fragment = idea.IdeaPluginGenerator {
			//Only the first extension will be used 
			fileExtensions = file.extensions

			//Path to generating Intellij IDEA Language Plugin; you will open it later via Intellij Idea
			pathIdeaPluginProject = "${runtimeProject}.idea"

			//Paths to required dependencies; check that specified dependencies exist
			library = "${pluginsPath}/${projectName}_1.0.0.201212271444.jar"
			library = "${pluginsPath}/org.eclipse.xtext_2.3.1.v201208210947.jar"
			library = "${pluginsPath}/org.eclipse.emf.ecore_2.8.0.v20120606-0717.jar"
			library = "${pluginsPath}/org.eclipse.emf.common_2.8.0.v20120606-0717.jar"
			library = "${pluginsPath}/org.antlr.runtime_3.2.0.v201101311130.jar"
			library = "${pluginsPath}/org.apache.log4j_1.2.15.v201012070815.jar"
			library = "${pluginsPath}/org.eclipse.xtext.generator_2.3.1.v201208210947.jar"
			library = "${pluginsPath}/org.eclipse.xtext.generator.idea_1.0.0.jar"
		}
	}
}
```

**5. Run Language Generation workflow and export plugins again.**

You will be able to find generated files under src-gen folder. For example for domain model language check org.example.domainmodel.domainmodel.lang package.

**6. Open Intellij IDEA (supported version 12 and above) and open the generated Intellij IDEA language plugin.**

1. File -> Open... -> Select the generated Intellij IDEA language plugin directory -> Press OK
2. Open Project Structure (Ctrl + Alt + Shift + S)
3. Project Settings -> Project -> Check that Project SDK is valid, if Project SDK is invalid then press NEW -> Intellij IDEA Plugin SDK -> Select Intellij IDEA directory -> OK -> OK
4. Project Settings -> Modules -> open Dependencies tab -> check that all dependencies and module SDK are valid
5. If Module SDK is invalid then choose valid SDK (created on the third step)
6. If dependency is invalid then remove it and add valid version of that dependency

List of required dependencies:
* your language eclipse plugin, for example: org.example.domainmodel_x.jar;
* org.antlr.runtime_x.jar
* org.eclipse.xtext_x.jar
* org.eclipse.xtext.generator_x.jar
* org.eclipse.emf.ecore_x.jar
* org.eclipse.emf.common_x.jar
* org.apache.log4j_x.jar
* org.eclipse.xtext.generator.idea_x.jar

**7. Run Intellij IDEA Language Plugin.**

1. Run -> Edit Configurations -> Add New Configuration (Insert) -> Plugin -> specify Name, for example “run DomainModel” -> Apply -> OK
2. Select the created configuration and press Run (Shift + F10)
3. Create new java project
4. Create file with your language extension
5. Open and try to edit it
6. Intellij Idea will highlight keywords, comments, strings and syntactical errors

**8. Deploy Intellij IDEA Language Plugin**

1. Build -> Prepare All Plugin Modules for Deployment -> OK -> OK -> Settings -> Plugins -> Install plugin from disk... -> Go to the plugin directory and select a created zip archive -> Ok -> Ok -> Restart
2. Create file with your language extension and edit it
