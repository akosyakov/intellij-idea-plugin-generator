package org.eclipse.xtext.generator.idea;

import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.generator.Generator;
import org.eclipse.xtext.generator.Naming;
import org.eclipse.xtext.generator.parser.antlr.AbstractAntlrGeneratorFragment;

public class PsiAntlrGeneratorFragment extends AbstractAntlrGeneratorFragment {

	public void generate(Grammar grammar, XpandExecutionContext ctx) {
		super.generate(grammar, ctx);
		String srcGenPath = ctx.getOutput().getOutlet(Generator.SRC_GEN).getPath();
		String absoluteGrammarFileName = srcGenPath + "/" + getGrammarFileName(grammar, getNaming()).replace('.', '/') + ".g";
		addAntlrParam("-fo");
		addAntlrParam(absoluteGrammarFileName.substring(0, absoluteGrammarFileName.lastIndexOf('/')));
		getAntlrTool().runWithParams(absoluteGrammarFileName, getAntlrParams());
		simplifyUnorderedGroupPredicatesIfRequired(grammar, absoluteGrammarFileName);
		splitParserAndLexerIfEnabled(absoluteGrammarFileName);
		suppressWarnings(absoluteGrammarFileName);
	}

	public static String getGrammarFileName(Grammar g, Naming naming) {
		String grammarName = GrammarUtil.getName(g);
		return naming.basePackageRuntime(g) + "." + grammarName.toLowerCase() + ".lang.parsing.Psi" + grammarName;
	}

}
