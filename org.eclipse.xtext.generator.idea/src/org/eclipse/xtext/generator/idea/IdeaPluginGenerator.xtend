package org.eclipse.xtext.generator.idea

import java.util.Set
import javax.inject.Inject
import org.eclipse.xpand2.output.Outlet
import org.eclipse.xpand2.output.Output
import org.eclipse.xpand2.output.OutputImpl
import org.eclipse.xtext.AbstractRule
import org.eclipse.xtext.Grammar
import org.eclipse.xtext.generator.Generator
import org.eclipse.xtext.generator.Xtend2ExecutionContext
import org.eclipse.xtext.generator.Xtend2GeneratorFragment

import static org.eclipse.xtext.generator.idea.IdeaPluginGenerator.*

class IdeaPluginGenerator extends Xtend2GeneratorFragment {
	
	private static String META_INF_PLUGIN = "META_INF_PLUGIN"
	
	private static String PLUGIN = "PLUGIN"
	
	private static String DOT_IDEA = "DOT_IDEA"
	
	private String encoding
	
	private String fileExtension
	
	private Set<String> libraries
	
	private String pathIdeaPluginProject
	
	@Inject
	extension IdeaPluginExtension
	
	override generate(Grammar grammar, Xtend2ExecutionContext ctx) {
//		for (rule:grammar.rules) {
//			ctx.writeFile(Generator::SRC_GEN, grammar.getPsiElementPath(rule), grammar.compilePsiElement(rule))
//			ctx.writeFile(Generator::SRC_GEN, grammar.getPsiElementImplPath(rule), grammar.compilePsiElementImpl(rule))	
//		}
		ctx.writeFile(Generator::SRC_GEN, grammar.languagePath, grammar.compileLanguage)
		ctx.writeFile(Generator::SRC_GEN, grammar.fileTypePath, grammar.compileFileType)
		ctx.writeFile(Generator::SRC_GEN, grammar.fileTypeFactoryPath, grammar.compileFileTypeFactory)
		ctx.writeFile(Generator::SRC_GEN, grammar.fileImplPath, grammar.compileFileImpl)
		ctx.writeFile(Generator::SRC_GEN, grammar.tokenTypesPath, grammar.compileTokenTypes);
		ctx.writeFile(Generator::SRC_GEN, grammar.elementTypesPath, grammar.compileElementTypes);
		ctx.writeFile(Generator::SRC_GEN, grammar.lexerPath, grammar.compileLexer);
		ctx.writeFile(Generator::SRC_GEN, grammar.parserPath, grammar.compilePsiParser);
		ctx.writeFile(Generator::SRC_GEN, grammar.tokenTypeProviderPath, grammar.compileTokenTypeProvider);
		ctx.writeFile(Generator::SRC_GEN, grammar.parserDefinitionPath, grammar.compileParserDefinition);
		ctx.writeFile(Generator::SRC_GEN, grammar.syntaxHighlighterPath, grammar.compileSyntaxHighlighter);
		ctx.writeFile(Generator::SRC_GEN, grammar.syntaxHighlighterFactoryPath, grammar.compileSyntaxHighlighterFactory);
		
		if (pathIdeaPluginProject != null) {
			var output = new OutputImpl();
			output.addOutlet(PLUGIN, pathIdeaPluginProject);
			output.addOutlet(META_INF_PLUGIN, pathIdeaPluginProject + "/META-INF");
			output.addOutlet(DOT_IDEA, pathIdeaPluginProject + "/.idea");
			
			output.writeFile(META_INF_PLUGIN, "plugin.xml", grammar.compilePluginXml)
			output.writeFile(PLUGIN, iml, grammar.compileIml)
			output.writeFile(DOT_IDEA, "modules.xml", grammar.compileModulesXml);
			output.writeFile(DOT_IDEA, "misc.xml", grammar.compileMiscXml);
		}
	}
	
	def iml() {
		pathIdeaPluginProject.substring(pathIdeaPluginProject.lastIndexOf("/") + 1) + ".iml"
	}
	
	def addOutlet(Output output, String outletName, String path) {
		output.addOutlet(new Outlet(false, getEncoding(), outletName, false, path))
	}
	
	def writeFile(Output output, String outletName, String filename, CharSequence contents) {
		output.openFile(filename, outletName);
		output.write(contents.toString);
		output.closeFile();
	}
	
	def getEncoding() {
		if (encoding != null) {
			return encoding;
		}
		return System::getProperty("file.encoding");
	}
	
	def addLibrary(String library) {
		if (libraries == null) {
			libraries = newHashSet();
		}
		libraries.add(library)
	}
	
	def setFileExtensions(String fileExtensions) {
		this.fileExtension = fileExtensions.split("\\s*,\\s*").get(0)
	}
	
	def setEncoding(String encoding) {
		this.encoding = encoding
	}
	
	def setPathIdeaPluginProject(String pathIdeaPluginProject) {
		this.pathIdeaPluginProject = pathIdeaPluginProject
	}
	
	def compileModulesXml(Grammar grammar)'''
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectModuleManager">
    <modules>
      <module fileurl="file://$PROJECT_DIR$/«iml»" filepath="$PROJECT_DIR$/«iml»" />
    </modules>
  </component>
</project>
	'''
	
	def compileMiscXml(Grammar grammar)'''
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectRootManager" version="2" languageLevel="JDK_1_6" assert-keyword="true" jdk-15="true" project-jdk-name="IDEA IC-123.72" project-jdk-type="IDEA JDK" />
  <output url="file://$PROJECT_DIR$/out" />
</project>
	'''
	
	def compilePluginXml(Grammar grammar)'''
<idea-plugin version="2">
	<id>«grammar.languageID»</id>
	<name>«grammar.simpleName» Support</name>
	<description>
      This plugin enables smart editing of «grammar.simpleName» files.
	</description>
	<version>1.0.0</version>
	<vendor>My Company</vendor>

	<idea-version since-build="123.72"/>

	<extensions defaultExtensionNs="com.intellij">
		<lang.syntaxHighlighterFactory key="«grammar.languageID»" implementationClass="«grammar.langPackageName».«grammar.syntaxHighlighterFactoryClassName»"/>
		<lang.parserDefinition language="«grammar.languageID»" implementationClass="«grammar.parsingPackageName».«grammar.parserDefinitionClassName»"/>
		<fileTypeFactory implementation="«grammar.langPackageName».«grammar.fileTypeFactoryClassName»"/>
	</extensions>

</idea-plugin>
	'''
	
	def compileIml(Grammar grammar)'''
<?xml version="1.0" encoding="UTF-8"?>
<module type="PLUGIN_MODULE" version="4">
  <component name="DevKit.ModuleBuildProperties" url="file://$MODULE_DIR$/META-INF/plugin.xml" />
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src" isTestSource="false" />
      <sourceFolder url="file://$MODULE_DIR$/test" isTestSource="true" />
    </content>
 	<orderEntry type="jdk" jdkName="IDEA IC-123.72" jdkType="IDEA JDK" />
    <orderEntry type="sourceFolder" forTests="false" />
    «FOR library:libraries»
    <orderEntry type="module-library">
      <library>
        <CLASSES>
          <root url="jar://«library»!/" />
        </CLASSES>
        <JAVADOC />
        <SOURCES />
      </library>
    </orderEntry>
    «ENDFOR»
  </component>
</module>
	'''
	
	def compilePsiElement(Grammar grammar, AbstractRule rule)'''
package «grammar.psiPackageName»;
«IF rule.hasMultipleAssigment»

import java.util.List;
«ENDIF»

import com.intellij.psi.«rule.psiElementSuperClassName»;

public interface «rule.psiElementClassName» extends «rule.psiElementSuperClassName» {
	«FOR assignment:rule.assignmentsWithoutName»
	
	«assignment.typeName» «assignment.getter»();
	
	void «assignment.setter»(«assignment.typeName» «assignment.feature»);
	«ENDFOR»

}
	'''
	
	def compilePsiElementImpl(Grammar grammar, AbstractRule rule)'''
package «grammar.psiImplPackageName»;
«IF rule.hasMultipleAssigment»

import java.util.ArrayList;
import java.util.List;
«ENDIF»

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
«IF rule.hasMultipleAssigment»
import com.intellij.psi.tree.TokenSet;
«ENDIF»
«IF !rule.assignments.empty»
import com.intellij.util.IncorrectOperationException;
import «grammar.parsingPackageName».«grammar.elementTypesClassName»;
«ENDIF»
import «grammar.psiPackageName».«rule.psiElementClassName»;
«FOR assignment:rule.multipleAssignments.toSet»
import «grammar.psiPackageName».«assignment.psiElementClassName»;
«ENDFOR»

public class «rule.psiElementImplClassName» extends ASTWrapperPsiElement implements «rule.psiElementClassName» {

	public «rule.psiElementImplClassName»(ASTNode node) {
		super(node);
	}
	«IF rule.hasName»

	public String getName() {
		ASTNode node = getNode().findChildByType(«grammar.elementTypesClassName».«rule.instanceName»_NAME);
		if (node == null) {
			return "";
		}
		return node.getText();
	}
	
	public «rule.psiElementClassName» setName(String name) throws IncorrectOperationException {
		throw new IncorrectOperationException();
	}
	«ENDIF»
	«FOR assignment:rule.assignmentsWithoutName»

	public «assignment.typeName» «assignment.getter»() {
		«IF assignment.multiple»
		«assignment.typeName» «assignment.feature» = new ArrayList<«assignment.internalTypeName»>();
		ASTNode[] nodes = getNode().getChildren(TokenSet.create(«grammar.elementTypesClassName».«assignment.ruleInstanceName»));
		if (nodes == null) {
			return «assignment.feature»;
		}
		for (ASTNode node : nodes) {
			«assignment.feature».add(new «assignment.psiElementImplClassName»(node));
		}
		return «assignment.feature»;
		«ELSE»
		ASTNode node = getNode().findChildByType(«grammar.elementTypesClassName».«rule.getInstanceName(assignment)»);
		if (node == null) {
			return «IF assignment.isBoolean»false«ELSE»""«ENDIF»;
		}
		return «IF assignment.isBoolean»true«ELSE»node.getText()«ENDIF»;
		«ENDIF»
	}

	public void «assignment.setter»(«assignment.typeName» value) {
		throw new IncorrectOperationException();
	}
	«ENDFOR»

}
	'''
	
	def compileFileImpl(Grammar grammar)'''
package «grammar.psiImplPackageName»;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import «grammar.langPackageName».«grammar.fileTypeClassName»;
import «grammar.langPackageName».«grammar.languageClassName»;

public final class «grammar.fileImplClassName» extends PsiFileBase {

	public «grammar.fileImplClassName»(FileViewProvider viewProvider) {
		super(viewProvider, «grammar.languageClassName».INSTANCE);
	}

	public FileType getFileType() {
		return «grammar.fileTypeClassName».INSTANCE;
	}

}
	'''
	
	def compileFileTypeFactory(Grammar grammar)'''
package «grammar.langPackageName»;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class «grammar.fileTypeFactoryClassName» extends FileTypeFactory {

	public void createFileTypes(@NotNull FileTypeConsumer consumer) {
		consumer.consume(«grammar.fileTypeClassName».INSTANCE, «grammar.fileTypeClassName».DEFAULT_EXTENSION);
	}

}
	'''
	
	def compileFileType(Grammar grammar)'''
package «grammar.langPackageName»;

import javax.swing.Icon;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;

public final class «grammar.fileTypeClassName» extends LanguageFileType {

	public static final «grammar.fileTypeClassName» INSTANCE = new «grammar.fileTypeClassName»();
	
	@NonNls 
	public static final String DEFAULT_EXTENSION = "«fileExtension»";

	private «grammar.fileTypeClassName»() {
		super(«grammar.languageClassName».INSTANCE);
	}

	public String getDefaultExtension() {
		return DEFAULT_EXTENSION;
	}

	public String getDescription() {
		return "«grammar.simpleName» files";
	}

	public Icon getIcon() {
		return null;
	}

	public String getName() {
		return "«grammar.simpleName»";
	}

}
	'''
	
	def compileLanguage(Grammar grammar)'''
package «grammar.langPackageName»;

import com.intellij.lang.Language;

public final class «grammar.languageClassName» extends Language {

	public static final «grammar.languageClassName» INSTANCE = new «grammar.languageClassName»();

	private «grammar.languageClassName»() {
		super("«grammar.languageID»", "«grammar.languageMimeType»");
	}

}
	'''
	
	def compileTokenTypes(Grammar grammar)'''
package «grammar.parsingPackageName»;

import static «grammar.parsingPackageName».«grammar.antlrParserClassName».tokenNames;

import java.util.HashMap;
import java.util.Map;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import «grammar.langPackageName».«grammar.languageClassName»;

public abstract class «grammar.tokenTypesClassName» {

	public static final IElementType[] tokenTypes = new IElementType[tokenNames.length];
	
	public static final Map<String, IElementType> nameToTypeMap = new HashMap<String, IElementType>();

	static {
		for (int i = 0; i < tokenNames.length; i++) {
			tokenTypes[i] = new IElementType(tokenNames[i], «grammar.languageClassName».INSTANCE);
			nameToTypeMap.put(tokenNames[i], tokenTypes[i]);
		}
	}

	public static final TokenSet COMMENTS = TokenSet.create(tokenTypes[«grammar.antlrParserClassName».RULE_SL_COMMENT],
			tokenTypes[«grammar.antlrParserClassName».RULE_ML_COMMENT]);
	
	public static final TokenSet LINE_COMMENTS = TokenSet.create(tokenTypes[«grammar.antlrParserClassName».RULE_SL_COMMENT]);
	
	public static final TokenSet BLOCK_COMMENTS = TokenSet.create(tokenTypes[«grammar.antlrParserClassName».RULE_ML_COMMENT]);

	public static final TokenSet WHITESPACES = TokenSet.create(tokenTypes[«grammar.antlrParserClassName».RULE_WS]);

	public static final TokenSet STRINGS = TokenSet.create(tokenTypes[«grammar.antlrParserClassName».RULE_STRING]);

}
	'''
	
	def compileElementTypes(Grammar grammar)'''
package «grammar.parsingPackageName»;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import «grammar.langPackageName».«grammar.languageClassName»;

public interface «grammar.elementTypesClassName» {

	IFileElementType FILE = new IFileElementType(«grammar.languageClassName».INSTANCE);
	«grammar.internalCompileElementTypes»

}
	'''
	
	def internalCompileElementTypes(Grammar grammar)'''
	«FOR rule:grammar.allRules»

	IElementType «rule.instanceName» = new IElementType("«rule.instanceName»", «grammar.languageClassName».INSTANCE);
	«FOR instanceName:rule.assignments.map(a | a.instanceName).toSet»

	IElementType «instanceName» = new IElementType("«instanceName»", «grammar.languageClassName».INSTANCE);
	«ENDFOR»
	«FOR instanceName:rule.enumLiteralDeclarations.map(a | a.instanceName).toSet»
	
	IElementType «instanceName» = new IElementType("«instanceName»", «grammar.languageClassName».INSTANCE);
	«ENDFOR»
	«ENDFOR»
	'''
	
	def compileLexer(Grammar grammar)'''
package «grammar.parsingPackageName»;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;

public class «grammar.lexerClassName» extends LexerBase {

    private «grammar.antlrLexerClassName» internalLexer;
    private CommonToken token;

    private CharSequence buffer;
    private int startOffset;
    private int endOffset;

    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.startOffset = startOffset;
        this.endOffset = endOffset;

        String text = buffer.subSequence(startOffset, endOffset).toString();
        internalLexer = new «grammar.antlrLexerClassName»(new ANTLRStringStream(text));
    }

    public int getState() {
        return token != null ? token.getType() : 0;
    }

    public IElementType getTokenType() {
        locateToken();
        if (token == null) {
            return null;
        }
        int type = token.getType();
        return «grammar.tokenTypesClassName».tokenTypes[type];
    }

    public int getTokenStart() {
        locateToken();
        return startOffset + token.getStartIndex();
    }

    public int getTokenEnd() {
        locateToken();
        return startOffset + token.getStopIndex() + 1;
    }

    public void advance() {
        locateToken();
        token = null;
    }

    public CharSequence getBufferSequence() {
        return buffer;
    }

    public int getBufferEnd() {
        return endOffset;
    }

    private void locateToken() {
        if (token == null) {
            try {
                token = (CommonToken) internalLexer.nextToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (token == Token.EOF_TOKEN) {
                token = null;
            }
        }
    }

}
	'''
	
	def compileTokenTypeProvider(Grammar grammar)'''
package «grammar.parsingPackageName»;

import java.util.Arrays;
import java.util.List;

import org.eclipse.xtext.generator.idea.TokenTypeProvider;

import com.intellij.psi.tree.IElementType;

public class «grammar.tokenTypeProviderClassName» implements TokenTypeProvider {

    public static final List<IElementType> I_ELEMENT_TYPES = Arrays.asList(«grammar.tokenTypesClassName».tokenTypes);

    public int getType(IElementType iElementType) {
        return I_ELEMENT_TYPES.indexOf(iElementType);
    }

}
	'''
	
	def compilePsiParser(Grammar grammar)'''
package «grammar.parsingPackageName»;

import org.antlr.runtime.RecognitionException;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

public class «grammar.parserClassName» implements PsiParser {

	public ASTNode parse(IElementType root, PsiBuilder builder) {
        PsiBuilder.Marker rootMarker = builder.mark();
        try {
            «grammar.antlrParserClassName» «grammar.antlrParserClassName.toFirstLower» = new «grammar.antlrParserClassName»(builder, new «grammar.tokenTypeProviderClassName»());
            «grammar.antlrParserClassName.toFirstLower».entryRule«grammar.rules.get(0).name»();
			«grammar.antlrParserClassName.toFirstLower».appendErrorMessage();
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
        while (!builder.eof()) {
            builder.advanceLexer();
        }
        rootMarker.done(root);
        return builder.getTreeBuilt();
	}

}
	'''

	def compileSyntaxHighlighterFactory(Grammar grammar)'''
package «grammar.langPackageName»;

import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

public class «grammar.syntaxHighlighterFactoryClassName» extends SingleLazyInstanceSyntaxHighlighterFactory {

    @NotNull
    protected SyntaxHighlighter createHighlighter() {
        return new «grammar.syntaxHighlighterClassName»();
    }

}
	'''
	
	def compileSyntaxHighlighter(Grammar grammar)'''
package «grammar.langPackageName»;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import «grammar.parsingPackageName».«grammar.lexerClassName»;
import «grammar.parsingPackageName».«grammar.tokenTypesClassName»;
import org.jetbrains.annotations.NotNull;

public class «grammar.syntaxHighlighterClassName» extends SyntaxHighlighterBase {

    @NotNull
    public Lexer getHighlightingLexer() {
        return new «grammar.lexerClassName»();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if («grammar.tokenTypesClassName».STRINGS.contains(tokenType)) {
            return pack(SyntaxHighlighterColors.STRING);
        }
		if («grammar.tokenTypesClassName».LINE_COMMENTS.contains(tokenType)) {
			return pack(SyntaxHighlighterColors.LINE_COMMENT);
		}
		if («grammar.tokenTypesClassName».BLOCK_COMMENTS.contains(tokenType)) {
			return pack(SyntaxHighlighterColors.JAVA_BLOCK_COMMENT);
		}
        String myDebugName = tokenType.toString();
		if (myDebugName.matches("^'.*\\w.*'$")) {
			return pack(SyntaxHighlighterColors.KEYWORD);
        }
        return new TextAttributesKey[0];
    }

}
	'''
	
	def compileParserDefinition(Grammar grammar)'''
package «grammar.parsingPackageName»;

import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
«««import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import «grammar.psiImplPackageName».«grammar.fileImplClassName»;

public class «grammar.parserDefinitionClassName» implements ParserDefinition {

	@NotNull
	public Lexer createLexer(Project project) {
		return new «grammar.lexerClassName»();
	}

	public IFileElementType getFileNodeType() {
		return «grammar.elementTypesClassName».FILE;
	}

	@NotNull
	public TokenSet getWhitespaceTokens() {
		return «grammar.tokenTypesClassName».WHITESPACES;
	}

	@NotNull
	public TokenSet getCommentTokens() {
		return «grammar.tokenTypesClassName».COMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements() {
		return «grammar.tokenTypesClassName».STRINGS;
	}

	@NotNull
	public PsiParser createParser(Project project) {
		return new «grammar.parserClassName»();
	}

	public PsiFile createFile(FileViewProvider viewProvider) {
		return new «grammar.fileImplClassName»(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}

	@NotNull
	public PsiElement createElement(ASTNode node) {
«««		IElementType type = node.getElementType();
«««		«FOR rule:grammar.rules»
«««		if (type == «grammar.elementTypesClassName».«rule.instanceName») {
«««			return new «rule.getPsiElementImplClassName»(node);
«««		} 
«««		«ENDFOR»
		return new ASTWrapperPsiElement(node);
	}

}
	'''
	
}