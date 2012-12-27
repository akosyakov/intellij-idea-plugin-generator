package org.eclipse.xtext.generator.idea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.EnumLiteralDeclaration;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class IdeaPluginExtension {
  private static String ELEMENT_TYPES = "PsiElementTypes";
  
  private static String TOKEN_TYPES = "TokenTypes";
  
  private static String LANGUAGE = "Language";
  
  private static String FILE_TYPE = "FileType";
  
  private static String FILE_TYPE_FACTORY = "FileTypeFactory";
  
  private static String FILE_IMPL = "FileImpl";
  
  private static String PARSER = "Parser";
  
  private static String TOKEN_TYPE_PROVIDER = "TokenTypeProvider";
  
  private static String LEXER = "Lexer";
  
  private static String PARSER_DEFINITION = "ParserDefinition";
  
  private static String SYNTAX_HIGHLIGHTER = "SyntaxHighlighter";
  
  private static String SYNTAX_HIGHLIGHTER_FACTORY = "SyntaxHighlighterFactory";
  
  private static String LANG_PACKAGE = ".lang";
  
  private static String PARSING_PACKAGE = new Function0<String>() {
    public String apply() {
      String _operator_plus = StringExtensions.operator_plus(IdeaPluginExtension.LANG_PACKAGE, ".parsing");
      return _operator_plus;
    }
  }.apply();
  
  private static String PSI_PACKAGE = ".lang.psi";
  
  private static String PSI_IMPL_PACKAGE = ".lang.psi.impl";
  
  private static String STUB_ELEMENT_TYPE = "StubElementType";
  
  public List<AbstractRule> getAllRules(final Grammar grammar) {
    ArrayList<AbstractRule> _newArrayList = CollectionLiterals.<AbstractRule>newArrayList();
    List<AbstractRule> _allRules = this.getAllRules(grammar, _newArrayList);
    return _allRules;
  }
  
  public List<AbstractRule> getAllRules(final Grammar grammar, final List<AbstractRule> rules) {
      EList<AbstractRule> _rules = grammar.getRules();
      final Function1<AbstractRule,Boolean> _function = new Function1<AbstractRule,Boolean>() {
          public Boolean apply(final AbstractRule e) {
            boolean _operator_and = false;
            boolean _operator_not = BooleanExtensions.operator_not((e instanceof TerminalRule));
            if (!_operator_not) {
              _operator_and = false;
            } else {
              final Function1<AbstractRule,Boolean> _function = new Function1<AbstractRule,Boolean>() {
                  public Boolean apply(final AbstractRule r) {
                    String _name = r.getName();
                    String _name_1 = e.getName();
                    boolean _equals = _name.equals(_name_1);
                    return Boolean.valueOf(_equals);
                  }
                };
              Iterable<AbstractRule> _filter = IterableExtensions.<AbstractRule>filter(rules, _function);
              boolean _isEmpty = IterableExtensions.isEmpty(_filter);
              _operator_and = BooleanExtensions.operator_and(_operator_not, _isEmpty);
            }
            return Boolean.valueOf(_operator_and);
          }
        };
      Iterable<AbstractRule> _filter = IterableExtensions.<AbstractRule>filter(_rules, _function);
      CollectionExtensions.<AbstractRule>addAll(rules, _filter);
      EList<Grammar> _usedGrammars = grammar.getUsedGrammars();
      for (final Grammar usedGrammar : _usedGrammars) {
        this.getAllRules(usedGrammar, rules);
      }
      return rules;
  }
  
  public String getSimpleName(final Grammar grammar) {
    String _name = GrammarUtil.getName(grammar);
    return _name;
  }
  
  public String getPackageName(final Grammar grammar) {
    String _namespace = GrammarUtil.getNamespace(grammar);
    return _namespace;
  }
  
  public String getPath(final String packageName) {
    String _replaceAll = packageName.replaceAll("\\.", "/");
    return _replaceAll;
  }
  
  public String getTokenTypesPath(final Grammar grammar) {
    String _parsingPackageName = this.getParsingPackageName(grammar);
    String _kenTypesClassName = this.getTokenTypesClassName(grammar);
    String _filePath = this.getFilePath(_parsingPackageName, _kenTypesClassName);
    return _filePath;
  }
  
  public String getElementTypesPath(final Grammar grammar) {
    String _parsingPackageName = this.getParsingPackageName(grammar);
    String _elementTypesClassName = this.getElementTypesClassName(grammar);
    String _filePath = this.getFilePath(_parsingPackageName, _elementTypesClassName);
    return _filePath;
  }
  
  public String getLexerPath(final Grammar grammar) {
    String _parsingPackageName = this.getParsingPackageName(grammar);
    String _lexerClassName = this.getLexerClassName(grammar);
    String _filePath = this.getFilePath(_parsingPackageName, _lexerClassName);
    return _filePath;
  }
  
  public String getParserPath(final Grammar grammar) {
    String _parsingPackageName = this.getParsingPackageName(grammar);
    String _parserClassName = this.getParserClassName(grammar);
    String _filePath = this.getFilePath(_parsingPackageName, _parserClassName);
    return _filePath;
  }
  
  public String tokenTypeProviderPath(final Grammar grammar) {
    String _parsingPackageName = this.getParsingPackageName(grammar);
    String _kenTypeProviderClassName = this.getTokenTypeProviderClassName(grammar);
    String _filePath = this.getFilePath(_parsingPackageName, _kenTypeProviderClassName);
    return _filePath;
  }
  
  public String getParserDefinitionPath(final Grammar grammar) {
    String _parsingPackageName = this.getParsingPackageName(grammar);
    String _parserDefinitionClassName = this.getParserDefinitionClassName(grammar);
    String _filePath = this.getFilePath(_parsingPackageName, _parserDefinitionClassName);
    return _filePath;
  }
  
  public String getSyntaxHighlighterPath(final Grammar grammar) {
    String _langPackageName = this.getLangPackageName(grammar);
    String _syntaxHighlighterClassName = this.getSyntaxHighlighterClassName(grammar);
    String _filePath = this.getFilePath(_langPackageName, _syntaxHighlighterClassName);
    return _filePath;
  }
  
  public String getSyntaxHighlighterFactoryPath(final Grammar grammar) {
    String _langPackageName = this.getLangPackageName(grammar);
    String _syntaxHighlighterFactoryClassName = this.getSyntaxHighlighterFactoryClassName(grammar);
    String _filePath = this.getFilePath(_langPackageName, _syntaxHighlighterFactoryClassName);
    return _filePath;
  }
  
  public String getLanguagePath(final Grammar grammar) {
    String _langPackageName = this.getLangPackageName(grammar);
    String _languageClassName = this.getLanguageClassName(grammar);
    String _filePath = this.getFilePath(_langPackageName, _languageClassName);
    return _filePath;
  }
  
  public String getFileTypePath(final Grammar grammar) {
    String _langPackageName = this.getLangPackageName(grammar);
    String _fileTypeClassName = this.getFileTypeClassName(grammar);
    String _filePath = this.getFilePath(_langPackageName, _fileTypeClassName);
    return _filePath;
  }
  
  public String getFileTypeFactoryPath(final Grammar grammar) {
    String _langPackageName = this.getLangPackageName(grammar);
    String _fileTypeFactoryClassName = this.getFileTypeFactoryClassName(grammar);
    String _filePath = this.getFilePath(_langPackageName, _fileTypeFactoryClassName);
    return _filePath;
  }
  
  public String fileImplPath(final Grammar grammar) {
    String _psiImplPackageName = this.getPsiImplPackageName(grammar);
    String _fileImplClassName = this.getFileImplClassName(grammar);
    String _filePath = this.getFilePath(_psiImplPackageName, _fileImplClassName);
    return _filePath;
  }
  
  public String getPsiElementPath(final Grammar grammar, final AbstractRule abstractRule) {
    String _psiPackageName = this.getPsiPackageName(grammar);
    String _psiElementClassName = this.getPsiElementClassName(abstractRule);
    String _filePath = this.getFilePath(_psiPackageName, _psiElementClassName);
    return _filePath;
  }
  
  public String getPsiElementImplPath(final Grammar grammar, final AbstractRule abstractRule) {
    String _psiImplPackageName = this.getPsiImplPackageName(grammar);
    String _psiElementImplClassName = this.getPsiElementImplClassName(abstractRule);
    String _filePath = this.getFilePath(_psiImplPackageName, _psiElementImplClassName);
    return _filePath;
  }
  
  public String getFilePath(final String packageName, final String className) {
    String _operator_plus = StringExtensions.operator_plus(packageName, ".");
    String _path = this.getPath(_operator_plus);
    String _operator_plus_1 = StringExtensions.operator_plus(_path, className);
    String _operator_plus_2 = StringExtensions.operator_plus(_operator_plus_1, ".java");
    return _operator_plus_2;
  }
  
  public String getInstanceName(final AbstractRule abstractRule) {
    String _name = abstractRule.getName();
    String _upperCase = _name.toUpperCase();
    return _upperCase;
  }
  
  public String ruleName(final TerminalRule terminalRule) {
    String _instanceName = this.getInstanceName(terminalRule);
    String _operator_plus = StringExtensions.operator_plus("RULE_", _instanceName);
    return _operator_plus;
  }
  
  public String getRuleInstanceName(final Assignment assignment) {
    AbstractElement _terminal = assignment.getTerminal();
    AbstractRule _rule = ((RuleCall) _terminal).getRule();
    String _instanceName = this.getInstanceName(_rule);
    return _instanceName;
  }
  
  public String getInstanceName(final Assignment assignment) {
    AbstractRule _containingRule = GrammarUtil.containingRule(assignment);
    String _instanceName = this.getInstanceName(_containingRule, assignment);
    return _instanceName;
  }
  
  public String getInstanceName(final EnumLiteralDeclaration enumLiteralDeclaration) {
    AbstractRule _containingRule = GrammarUtil.containingRule(enumLiteralDeclaration);
    String _instanceName = this.getInstanceName(_containingRule, enumLiteralDeclaration);
    return _instanceName;
  }
  
  public String getInstanceName(final RuleCall ruleCall) {
    Assignment _containingAssignment = GrammarUtil.containingAssignment(ruleCall);
    String _instanceName = this.getInstanceName(_containingAssignment);
    return _instanceName;
  }
  
  public String getInstanceName(final AbstractRule rule, final Assignment assignment) {
    String _instanceName = this.getInstanceName(rule);
    String _operator_plus = StringExtensions.operator_plus(_instanceName, "_");
    String _feature = assignment.getFeature();
    String _upperCase = _feature.toUpperCase();
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _upperCase);
    return _operator_plus_1;
  }
  
  public String getInstanceName(final AbstractRule rule, final EnumLiteralDeclaration enumLiteralDeclaration) {
    String _instanceName = this.getInstanceName(rule);
    String _operator_plus = StringExtensions.operator_plus(_instanceName, "_");
    EEnumLiteral _enumLiteral = enumLiteralDeclaration.getEnumLiteral();
    String _literal = _enumLiteral.getLiteral();
    String _upperCase = _literal.toUpperCase();
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _upperCase);
    return _operator_plus_1;
  }
  
  public String getPsiElementImplClassName(final Grammar grammar, final AbstractRule abstractRule) {
    String _psiImplPackageName = this.getPsiImplPackageName(grammar);
    String _operator_plus = StringExtensions.operator_plus(_psiImplPackageName, ".");
    String _psiElementImplClassName = this.getPsiElementImplClassName(abstractRule);
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _psiElementImplClassName);
    return _operator_plus_1;
  }
  
  public String getPsiElementImplClassName(final AbstractRule abstractRule) {
    String _name = abstractRule.getName();
    String _operator_plus = StringExtensions.operator_plus(_name, "Impl");
    return _operator_plus;
  }
  
  public String getPsiElementClassName(final AbstractRule abstractRule) {
    String _name = abstractRule.getName();
    return _name;
  }
  
  public String getPsiElementSuperClassName(final AbstractRule abstractRule) {
      boolean _hasName = this.hasName(abstractRule);
      if (_hasName) {
        return "PsiNamedElement";
      }
      return "PsiElement";
  }
  
  public boolean hasName(final AbstractRule rule) {
    List<Assignment> _assignments = this.getAssignments(rule);
    final Function1<Assignment,Boolean> _function = new Function1<Assignment,Boolean>() {
        public Boolean apply(final Assignment a) {
          String _feature = a.getFeature();
          boolean _equals = _feature.equals("name");
          return Boolean.valueOf(_equals);
        }
      };
    Iterable<Assignment> _filter = IterableExtensions.<Assignment>filter(_assignments, _function);
    boolean _isEmpty = IterableExtensions.isEmpty(_filter);
    boolean _operator_not = BooleanExtensions.operator_not(_isEmpty);
    return _operator_not;
  }
  
  public List<Assignment> getAssignmentsWithoutName(final AbstractRule rule) {
    List<Assignment> _assignments = this.getAssignments(rule);
    final Function1<Assignment,Boolean> _function = new Function1<Assignment,Boolean>() {
        public Boolean apply(final Assignment a) {
          String _feature = a.getFeature();
          boolean _equals = _feature.equals("name");
          boolean _operator_not = BooleanExtensions.operator_not(_equals);
          return Boolean.valueOf(_operator_not);
        }
      };
    Iterable<Assignment> _filter = IterableExtensions.<Assignment>filter(_assignments, _function);
    List<Assignment> _list = IterableExtensions.<Assignment>toList(_filter);
    return _list;
  }
  
  public List<Assignment> getAssignments(final AbstractRule rule) {
    TreeIterator<EObject> _eAllContents = rule.eAllContents();
    Iterator<Assignment> _filter = IteratorExtensions.<Assignment>filter(_eAllContents, org.eclipse.xtext.Assignment.class);
    final Function1<Assignment,Boolean> _function = new Function1<Assignment,Boolean>() {
        public Boolean apply(final Assignment a) {
          AbstractRule _containingRule = GrammarUtil.containingRule(a);
          boolean _equals = _containingRule.equals(rule);
          return Boolean.valueOf(_equals);
        }
      };
    Iterator<Assignment> _filter_1 = IteratorExtensions.<Assignment>filter(_filter, _function);
    List<Assignment> _list = IteratorExtensions.<Assignment>toList(_filter_1);
    return _list;
  }
  
  public List<EnumLiteralDeclaration> getEnumLiteralDeclarations(final AbstractRule rule) {
    TreeIterator<EObject> _eAllContents = rule.eAllContents();
    Iterator<EnumLiteralDeclaration> _filter = IteratorExtensions.<EnumLiteralDeclaration>filter(_eAllContents, org.eclipse.xtext.EnumLiteralDeclaration.class);
    final Function1<EnumLiteralDeclaration,Boolean> _function = new Function1<EnumLiteralDeclaration,Boolean>() {
        public Boolean apply(final EnumLiteralDeclaration a) {
          AbstractRule _containingRule = GrammarUtil.containingRule(a);
          boolean _equals = _containingRule.equals(rule);
          return Boolean.valueOf(_equals);
        }
      };
    Iterator<EnumLiteralDeclaration> _filter_1 = IteratorExtensions.<EnumLiteralDeclaration>filter(_filter, _function);
    List<EnumLiteralDeclaration> _list = IteratorExtensions.<EnumLiteralDeclaration>toList(_filter_1);
    return _list;
  }
  
  public String getLocalName(final AbstractRule abstractRule) {
    String _name = abstractRule.getName();
    String _firstLower = StringExtensions.toFirstLower(_name);
    return _firstLower;
  }
  
  public String getRuleLocalName(final AbstractElement element) {
    AbstractRule _containingRule = GrammarUtil.containingRule(element);
    String _localName = this.getLocalName(_containingRule);
    return _localName;
  }
  
  public String getRuleInstanceName(final AbstractElement element) {
    AbstractRule _containingRule = GrammarUtil.containingRule(element);
    String _instanceName = this.getInstanceName(_containingRule);
    return _instanceName;
  }
  
  public String getLocalName(final Assignment assignment) {
    AbstractRule _containingRule = GrammarUtil.containingRule(assignment);
    String _localName = this.getLocalName(_containingRule);
    String _feature = assignment.getFeature();
    String _firstUpper = StringExtensions.toFirstUpper(_feature);
    String _operator_plus = StringExtensions.operator_plus(_localName, _firstUpper);
    return _operator_plus;
  }
  
  public String getLocalName(final RuleCall ruleCall) {
    Assignment _containingAssignment = GrammarUtil.containingAssignment(ruleCall);
    String _localName = this.getLocalName(_containingAssignment);
    return _localName;
  }
  
  public boolean hasMultipleAssigment(final AbstractRule rule) {
    Iterable<Assignment> _multipleAssignments = this.getMultipleAssignments(rule);
    boolean _isEmpty = IterableExtensions.isEmpty(_multipleAssignments);
    boolean _operator_not = BooleanExtensions.operator_not(_isEmpty);
    return _operator_not;
  }
  
  public Iterable<Assignment> getMultipleAssignments(final AbstractRule rule) {
    List<Assignment> _assignments = this.getAssignments(rule);
    final Function1<Assignment,Boolean> _function = new Function1<Assignment,Boolean>() {
        public Boolean apply(final Assignment a) {
          boolean _isMultiple = IdeaPluginExtension.this.isMultiple(a);
          return Boolean.valueOf(_isMultiple);
        }
      };
    Iterable<Assignment> _filter = IterableExtensions.<Assignment>filter(_assignments, _function);
    return _filter;
  }
  
  public String getTypeName(final Assignment assignment) {
      boolean _isMultiple = this.isMultiple(assignment);
      if (_isMultiple) {
        String _internalTypeName = this.getInternalTypeName(assignment);
        String _operator_plus = StringExtensions.operator_plus("List<", _internalTypeName);
        String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, ">");
        return _operator_plus_1;
      }
      String _internalTypeName_1 = this.getInternalTypeName(assignment);
      return _internalTypeName_1;
  }
  
  public boolean isMultiple(final Assignment assignment) {
    String _operator = assignment.getOperator();
    boolean _equals = "+=".equals(_operator);
    return _equals;
  }
  
  public boolean isBoolean(final Assignment assignment) {
    String _operator = assignment.getOperator();
    boolean _equals = "?=".equals(_operator);
    return _equals;
  }
  
  public boolean isOneOrNone(final AbstractElement element) {
    String _cardinality = element.getCardinality();
    boolean _equals = "?".equals(_cardinality);
    return _equals;
  }
  
  public boolean isExactlyOne(final AbstractElement element) {
    String _cardinality = element.getCardinality();
    boolean _operator_equals = ObjectExtensions.operator_equals(_cardinality, null);
    return _operator_equals;
  }
  
  public boolean isAny(final AbstractElement element) {
    String _cardinality = element.getCardinality();
    boolean _equals = "*".equals(_cardinality);
    return _equals;
  }
  
  public boolean isOneOrMore(final AbstractElement element) {
    String _cardinality = element.getCardinality();
    boolean _equals = "+".equals(_cardinality);
    return _equals;
  }
  
  public String getVariableName(final AbstractElement abstartElement) {
    AbstractRule _containingRule = GrammarUtil.containingRule(abstartElement);
    TreeIterator<EObject> _eAllContents = _containingRule.eAllContents();
    Iterator<AbstractElement> _filter = IteratorExtensions.<AbstractElement>filter(_eAllContents, org.eclipse.xtext.AbstractElement.class);
    List<AbstractElement> _list = IteratorExtensions.<AbstractElement>toList(_filter);
    int _indexOf = _list.indexOf(abstartElement);
    String _operator_plus = StringExtensions.operator_plus("variable_", Integer.valueOf(_indexOf));
    return _operator_plus;
  }
  
  protected String _getInternalTypeName(final Assignment assignment) {
      boolean _isBoolean = this.isBoolean(assignment);
      if (_isBoolean) {
        return "boolean";
      }
      AbstractElement _terminal = assignment.getTerminal();
      String _internalTypeName = this.getInternalTypeName(_terminal);
      return _internalTypeName;
  }
  
  protected String _getInternalTypeName(final CrossReference crossReference) {
    AbstractElement _terminal = crossReference.getTerminal();
    String _internalTypeName = this.getInternalTypeName(_terminal);
    return _internalTypeName;
  }
  
  protected String _getInternalTypeName(final RuleCall ruleCall) {
      AbstractRule _rule = ruleCall.getRule();
      if ((_rule instanceof TerminalRule)) {
        return "String";
      }
      AbstractRule _rule_1 = ruleCall.getRule();
      String _name = _rule_1.getName();
      return _name;
  }
  
  protected String _getInternalTypeName(final AbstractElement abstractElement) {
    return "";
  }
  
  public String getGetter(final Assignment assignment) {
      boolean _isBoolean = this.isBoolean(assignment);
      if (_isBoolean) {
        String _feature = assignment.getFeature();
        String _firstUpper = StringExtensions.toFirstUpper(_feature);
        String _operator_plus = StringExtensions.operator_plus("is", _firstUpper);
        return _operator_plus;
      }
      String _feature_1 = assignment.getFeature();
      String _firstUpper_1 = StringExtensions.toFirstUpper(_feature_1);
      String _operator_plus_1 = StringExtensions.operator_plus("get", _firstUpper_1);
      return _operator_plus_1;
  }
  
  public String getSetter(final Assignment assignment) {
    String _feature = assignment.getFeature();
    String _firstUpper = StringExtensions.toFirstUpper(_feature);
    String _operator_plus = StringExtensions.operator_plus("set", _firstUpper);
    return _operator_plus;
  }
  
  public String getPsiElementImplClassName(final Assignment assignment) {
    AbstractElement _terminal = assignment.getTerminal();
    AbstractRule _rule = ((RuleCall) _terminal).getRule();
    String _psiElementImplClassName = this.getPsiElementImplClassName(_rule);
    return _psiElementImplClassName;
  }
  
  public String getPsiElementClassName(final Assignment assignment) {
    AbstractElement _terminal = assignment.getTerminal();
    AbstractRule _rule = ((RuleCall) _terminal).getRule();
    String _psiElementClassName = this.getPsiElementClassName(_rule);
    return _psiElementClassName;
  }
  
  public String getElementTypesClassName(final Grammar grammar) {
    return IdeaPluginExtension.ELEMENT_TYPES;
  }
  
  public String getTokenTypesClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.TOKEN_TYPES);
    return _className;
  }
  
  public String getLanguageClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.LANGUAGE);
    return _className;
  }
  
  public String getFileTypeClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.FILE_TYPE);
    return _className;
  }
  
  public String getFileTypeFactoryClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.FILE_TYPE_FACTORY);
    return _className;
  }
  
  public String getFileImplClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.FILE_IMPL);
    return _className;
  }
  
  public String getLanguageID(final Grammar grammar) {
    String _name = grammar.getName();
    return _name;
  }
  
  public String getLanguageMimeType(final Grammar grammar) {
    String _name = grammar.getName();
    String _operator_plus = StringExtensions.operator_plus("text/", _name);
    return _operator_plus;
  }
  
  public String getParserClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.PARSER);
    return _className;
  }
  
  public String getTokenTypeProviderClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.TOKEN_TYPE_PROVIDER);
    return _className;
  }
  
  public String getAntlrParserClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.PARSER);
    String _operator_plus = StringExtensions.operator_plus("Psi", _className);
    return _operator_plus;
  }
  
  public String getParserDefinitionClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.PARSER_DEFINITION);
    return _className;
  }
  
  public String getSyntaxHighlighterClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.SYNTAX_HIGHLIGHTER);
    return _className;
  }
  
  public String getSyntaxHighlighterFactoryClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.SYNTAX_HIGHLIGHTER_FACTORY);
    return _className;
  }
  
  public String getLexerClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.LEXER);
    return _className;
  }
  
  public String getAntlrLexerClassName(final Grammar grammar) {
    String _className = this.getClassName(grammar, IdeaPluginExtension.LEXER);
    String _operator_plus = StringExtensions.operator_plus("Psi", _className);
    return _operator_plus;
  }
  
  public String getClassName(final Grammar grammar, final String typeName) {
    String _simpleName = this.getSimpleName(grammar);
    String _operator_plus = StringExtensions.operator_plus(_simpleName, typeName);
    return _operator_plus;
  }
  
  public String getLangPackageName(final Grammar grammar) {
    String _packageName = this.getPackageName(grammar);
    String _operator_plus = StringExtensions.operator_plus(_packageName, IdeaPluginExtension.LANG_PACKAGE);
    return _operator_plus;
  }
  
  public String getParsingPackageName(final Grammar grammar) {
    String _packageName = this.getPackageName(grammar);
    String _operator_plus = StringExtensions.operator_plus(_packageName, IdeaPluginExtension.PARSING_PACKAGE);
    return _operator_plus;
  }
  
  public String getPsiImplPackageName(final Grammar grammar) {
    String _packageName = this.getPackageName(grammar);
    String _operator_plus = StringExtensions.operator_plus(_packageName, IdeaPluginExtension.PSI_IMPL_PACKAGE);
    return _operator_plus;
  }
  
  public String getPsiPackageName(final Grammar grammar) {
    String _packageName = this.getPackageName(grammar);
    String _operator_plus = StringExtensions.operator_plus(_packageName, IdeaPluginExtension.PSI_PACKAGE);
    return _operator_plus;
  }
  
  public String getStubElementTypeClassName(final AbstractRule abstractRule) {
    String _name = abstractRule.getName();
    String _operator_plus = StringExtensions.operator_plus(_name, IdeaPluginExtension.STUB_ELEMENT_TYPE);
    return _operator_plus;
  }
  
  public String getInternalTypeName(final AbstractElement assignment) {
    if (assignment instanceof Assignment) {
      return _getInternalTypeName((Assignment)assignment);
    } else if (assignment instanceof CrossReference) {
      return _getInternalTypeName((CrossReference)assignment);
    } else if (assignment instanceof RuleCall) {
      return _getInternalTypeName((RuleCall)assignment);
    } else if (assignment != null) {
      return _getInternalTypeName(assignment);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(assignment).toString());
    }
  }
}
