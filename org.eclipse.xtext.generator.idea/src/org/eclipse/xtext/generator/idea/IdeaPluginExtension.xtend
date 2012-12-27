package org.eclipse.xtext.generator.idea

import java.util.List
import org.eclipse.xtext.AbstractElement
import org.eclipse.xtext.AbstractRule
import org.eclipse.xtext.Assignment
import org.eclipse.xtext.CrossReference
import org.eclipse.xtext.EnumLiteralDeclaration
import org.eclipse.xtext.Grammar
import org.eclipse.xtext.GrammarUtil
import org.eclipse.xtext.RuleCall
import org.eclipse.xtext.TerminalRule

import static org.eclipse.xtext.generator.idea.IdeaPluginExtension.*

class IdeaPluginExtension {
	
	private static String ELEMENT_TYPES = "PsiElementTypes"
	
	private static String TOKEN_TYPES = "TokenTypes"
	
	private static String LANGUAGE = "Language"
	
	private static String FILE_TYPE = "FileType"
	
	private static String FILE_TYPE_FACTORY = "FileTypeFactory"
	
	private static String FILE_IMPL = "FileImpl"
	
	private static String PARSER = "Parser"
	
	private static String TOKEN_TYPE_PROVIDER = "TokenTypeProvider"
	
	private static String LEXER = "Lexer"
	
	private static String PARSER_DEFINITION = "ParserDefinition"
	
	private static String SYNTAX_HIGHLIGHTER = "SyntaxHighlighter"
	
	private static String SYNTAX_HIGHLIGHTER_FACTORY = "SyntaxHighlighterFactory"
	
	private static String LANG_PACKAGE = ".lang"
	
	private static String PARSING_PACKAGE = LANG_PACKAGE + ".parsing"
	
	private static String PSI_PACKAGE = ".lang.psi"
	
	private static String PSI_IMPL_PACKAGE = ".lang.psi.impl"
	
	private static String STUB_ELEMENT_TYPE = "StubElementType"	
	
	def getAllRules(Grammar grammar) {
		getAllRules(grammar, <AbstractRule>newArrayList())
	}
	
	def List<AbstractRule> getAllRules(Grammar grammar, List<AbstractRule> rules) {
		rules.addAll(grammar.rules.filter(e | !(e instanceof TerminalRule) && rules.filter(r | r.name.equals(e.name)).empty))
		for (usedGrammar : grammar.usedGrammars) {
			getAllRules(usedGrammar, rules);
		}
		return rules;
	}
	
	def getSimpleName(Grammar grammar) {
		grammar.name.substring(grammar.name.lastIndexOf(".") + 1)
	}
	
	def getPackageName(Grammar grammar) {
		grammar.name.toLowerCase;
	}
	
	def getPath(String packageName) {
		packageName.replaceAll("\\.", "/")
	}
	
	def getTokenTypesPath(Grammar grammar) {
		getFilePath(grammar.parsingPackageName, grammar.tokenTypesClassName)
	}
	
	def getElementTypesPath(Grammar grammar) {
		getFilePath(grammar.parsingPackageName, grammar.elementTypesClassName)
	}
	
	def getLexerPath(Grammar grammar) {
		getFilePath(grammar.parsingPackageName, grammar.lexerClassName)
	}
	
	def getParserPath(Grammar grammar) {
		getFilePath(grammar.parsingPackageName, grammar.parserClassName)
	}
	
	def tokenTypeProviderPath(Grammar grammar) {
		getFilePath(grammar.parsingPackageName, grammar.tokenTypeProviderClassName);
	}
	
	def getParserDefinitionPath(Grammar grammar) {
		getFilePath(grammar.parsingPackageName, grammar.parserDefinitionClassName)
	}
	
	def getSyntaxHighlighterPath(Grammar grammar) {
		getFilePath(grammar.langPackageName, grammar.syntaxHighlighterClassName)
	}
	
	def getSyntaxHighlighterFactoryPath(Grammar grammar) {
		getFilePath(grammar.langPackageName, grammar.syntaxHighlighterFactoryClassName)
	}
	
	def getLanguagePath(Grammar grammar) {
		getFilePath(grammar.langPackageName, grammar.languageClassName)
	}
	
	def getFileTypePath(Grammar grammar) {
		getFilePath(grammar.langPackageName, grammar.fileTypeClassName)
	}
	
	def getFileTypeFactoryPath(Grammar grammar) {
		getFilePath(grammar.langPackageName, grammar.fileTypeFactoryClassName);
	}
	
	def fileImplPath(Grammar grammar) {
		getFilePath(grammar.psiImplPackageName, grammar.fileImplClassName)
	}
	
	def getPsiElementPath(Grammar grammar, AbstractRule abstractRule) {
		getFilePath(grammar.psiPackageName, abstractRule.getPsiElementClassName);
	}
	
	def getPsiElementImplPath(Grammar grammar, AbstractRule abstractRule) {
		getFilePath(grammar.psiImplPackageName, abstractRule.getPsiElementImplClassName);
	}
	
	def getFilePath(String packageName, String className) {
		(packageName + ".").path + className + ".java"
	}
	
	def getInstanceName(AbstractRule abstractRule) {
		abstractRule.name.toUpperCase
	}
	
	def ruleName(TerminalRule terminalRule) {
		"RULE_" + terminalRule.instanceName
	}
	
	def getRuleInstanceName(Assignment assignment) {
		(assignment.terminal as RuleCall).rule.instanceName
	}
	
	def getInstanceName(Assignment assignment) {
		GrammarUtil::containingRule(assignment).getInstanceName(assignment)
	}
	
	def getInstanceName(EnumLiteralDeclaration enumLiteralDeclaration) {
		GrammarUtil::containingRule(enumLiteralDeclaration).getInstanceName(enumLiteralDeclaration)
	}
	
	def getInstanceName(RuleCall ruleCall) {
		GrammarUtil::containingAssignment(ruleCall).instanceName
	}
	
	def getInstanceName(AbstractRule rule, Assignment assignment) {
		rule.instanceName + "_" + assignment.feature.toUpperCase
	}
	
	def getInstanceName(AbstractRule rule, EnumLiteralDeclaration enumLiteralDeclaration) {
		rule.instanceName + "_" + enumLiteralDeclaration.enumLiteral.literal.toUpperCase
	}
	
	def getPsiElementImplClassName(Grammar grammar, AbstractRule abstractRule) {
		grammar.psiImplPackageName + "." + abstractRule.getPsiElementImplClassName
	}
	
	def getPsiElementImplClassName(AbstractRule abstractRule) {
		abstractRule.name + "Impl"
	}
	
	def getPsiElementClassName(AbstractRule abstractRule) {
		abstractRule.name
	}
	
	def getPsiElementSuperClassName(AbstractRule abstractRule) {
		if (abstractRule.hasName) {
			return "PsiNamedElement"
		}
		return "PsiElement"
	}
	
	def hasName(AbstractRule rule) {
		!rule.assignments.filter(a | a.feature.equals("name")).empty
	}
	
	def getAssignmentsWithoutName(AbstractRule rule) {
		rule.assignments.filter(a | !a.feature.equals("name")).toList
	}
	
	def getAssignments(AbstractRule rule) {
		rule.eAllContents.filter(typeof(Assignment)).filter(a | GrammarUtil::containingRule(a).equals(rule)).toList
	}
	
	def getEnumLiteralDeclarations(AbstractRule rule) {
		rule.eAllContents.filter(typeof(EnumLiteralDeclaration)).filter(a | GrammarUtil::containingRule(a).equals(rule)).toList
	}
	
	def getLocalName(AbstractRule abstractRule) {
		abstractRule.name.toFirstLower
	}
	
	def getRuleLocalName(AbstractElement element) {
		GrammarUtil::containingRule(element).localName
	}
	
	def getRuleInstanceName(AbstractElement element) {
		GrammarUtil::containingRule(element).instanceName
	}
	
	def getLocalName(Assignment assignment) {
		GrammarUtil::containingRule(assignment).localName + assignment.feature.toFirstUpper		
	}
	
	def getLocalName(RuleCall ruleCall) {
		GrammarUtil::containingAssignment(ruleCall).localName		
	}
	
	def hasMultipleAssigment(AbstractRule rule) {
		!rule.multipleAssignments.empty
	}
	
	def getMultipleAssignments(AbstractRule rule) {
		rule.assignments.filter(a | a.multiple)
	}
	
	def getTypeName(Assignment assignment) {
		if (assignment.multiple) {
			return "List<" + assignment.internalTypeName + ">";
		}
		return assignment.internalTypeName;
	}
	
	def isMultiple(Assignment assignment) {
		"+=".equals(assignment.operator)
	}
	
	def isBoolean(Assignment assignment) {
		"?=".equals(assignment.operator)
	}
	
	def isOneOrNone(AbstractElement element) {
		"?".equals(element.cardinality)
	}
	
	def isExactlyOne(AbstractElement element) {
		element.cardinality == null
	}
	
	def isAny(AbstractElement element) {
		"*".equals(element.cardinality)
	}
	
	def isOneOrMore(AbstractElement element) {
		"+".equals(element.cardinality)
	}
	
	def getVariableName(AbstractElement abstartElement) {
		"variable_" + GrammarUtil::containingRule(abstartElement).eAllContents.filter(typeof(AbstractElement)).toList.indexOf(abstartElement)
	}
	
	def dispatch String getInternalTypeName(Assignment assignment) {
		if (assignment.isBoolean) {
			return "boolean";
		}
		return assignment.terminal.internalTypeName;
	}
	
	def dispatch String getInternalTypeName(CrossReference crossReference) {
		crossReference.terminal.internalTypeName
	}
	
	def dispatch String getInternalTypeName(RuleCall ruleCall) {
		if (ruleCall.rule instanceof TerminalRule) {
			return "String";
		}
		return ruleCall.rule.name;
	}
	
	def dispatch String getInternalTypeName(AbstractElement abstractElement) {
		""
	}
	
	def getGetter(Assignment assignment) {
		if (assignment.isBoolean) {
			return "is" + assignment.feature.toFirstUpper	
		}
		return "get" + assignment.feature.toFirstUpper
	}
	
	def getSetter(Assignment assignment) {
		"set" + assignment.feature.toFirstUpper
	}
	
	def getPsiElementImplClassName(Assignment assignment) {
		(assignment.terminal as RuleCall).rule.psiElementImplClassName
	}
	
	def getPsiElementClassName(Assignment assignment) {
		(assignment.terminal as RuleCall).rule.psiElementClassName
	}
	
	def getElementTypesClassName(Grammar grammar) {
		ELEMENT_TYPES
	}
	
	def getTokenTypesClassName(Grammar grammar) {
		grammar.getClassName(TOKEN_TYPES);
	}
	
	def getLanguageClassName(Grammar grammar) {
		grammar.getClassName(LANGUAGE)
	}
	
	def getFileTypeClassName(Grammar grammar) {
		grammar.getClassName(FILE_TYPE)
	}
	
	def getFileTypeFactoryClassName(Grammar grammar) {
		grammar.getClassName(FILE_TYPE_FACTORY)
	}
	
	def getFileImplClassName(Grammar grammar) {
		grammar.getClassName(FILE_IMPL)
	}
	
	def getLanguageID(Grammar grammar) {
		grammar.name
	}
	
	def getLanguageMimeType(Grammar grammar) {
		"text/" + grammar.name
	}
	
	def getParserClassName(Grammar grammar) {
		grammar.getClassName(PARSER)
	}
	
	def getTokenTypeProviderClassName(Grammar grammar) {
		grammar.getClassName(TOKEN_TYPE_PROVIDER)
	}
	
	def getAntlrParserClassName(Grammar grammar) {
		"Psi" + grammar.getClassName(PARSER)
	}
	
	def getParserDefinitionClassName(Grammar grammar) {
		grammar.getClassName(PARSER_DEFINITION)
	}
	
	def getSyntaxHighlighterClassName(Grammar grammar) {
		grammar.getClassName(SYNTAX_HIGHLIGHTER)
	}
	
	def getSyntaxHighlighterFactoryClassName(Grammar grammar) {
		grammar.getClassName(SYNTAX_HIGHLIGHTER_FACTORY)
	}
	
	def getLexerClassName(Grammar grammar) {
		grammar.getClassName(LEXER)
	}
	
	def getAntlrLexerClassName(Grammar grammar) {
		"Psi" + grammar.getClassName(LEXER)
	}
	
	def getClassName(Grammar grammar, String typeName) {
		grammar.simpleName + typeName
	}
	
	def getLangPackageName(Grammar grammar) {
		grammar.packageName + LANG_PACKAGE
	}
	
	def getParsingPackageName(Grammar grammar) {
		grammar.packageName + PARSING_PACKAGE
	}
	
	def getPsiImplPackageName(Grammar grammar) {
		grammar.packageName + PSI_IMPL_PACKAGE
	}
	
	def getPsiPackageName(Grammar grammar) {
		grammar.packageName + PSI_PACKAGE		
	}
	
	def getStubElementTypeClassName(AbstractRule abstractRule) {
		abstractRule.name + STUB_ELEMENT_TYPE;
	}
	
}