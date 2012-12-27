package org.eclipse.xtext.generator.idea;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.MissingTokenException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.UnwantedTokenException;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.psi.tree.IElementType;

public class AbstractPsiAntlrParser extends BaseRecognizer {

	protected PsiBuilder psiBuilder;

	private String errorMessage;

	private PsiBuilder.Marker errorMarker;

	private Stack<PsiBuilder.Marker> ruleMarkers;

	private Stack<IElementType> ruleIElementTypes;

	private Map<PsiBuilder.Marker, PsiBuilder.Marker> markers;

	private Map<PsiBuilder.Marker, IElementType> iElementTypes;

	protected final TokenStream input;

	public AbstractPsiAntlrParser(PsiBuilder psiBuilder,
			TokenTypeProvider tokenTypeProvider) {
		this.psiBuilder = psiBuilder;
		this.ruleMarkers = new Stack<PsiBuilder.Marker>();
		this.ruleIElementTypes = new Stack<IElementType>();
		this.markers = new HashMap<PsiBuilder.Marker, PsiBuilder.Marker>();
		this.iElementTypes = new HashMap<PsiBuilder.Marker, IElementType>();
		input = new PsiTokenStream(psiBuilder, tokenTypeProvider);
	}

	public AbstractPsiAntlrParser(TokenStream input, RecognizerSharedState state) {
		super(state);
		this.input = input;
	}

	public void recover(IntStream input, RecognitionException re) {
		markError();
		input.consume();
		doneError(re);
		super.recover(input, re);
	}

	protected final void mark(IElementType iElementType) {
		Marker ruleMarker = ruleMarkers.peek();
		markers.put(ruleMarker, psiBuilder.mark());
		iElementTypes.put(ruleMarker, iElementType);
	}

	protected final void done() {
		Marker ruleMarker = ruleMarkers.peek();
		Marker marker = markers.get(ruleMarker);
		if (marker != null) {
			IElementType iElementType = iElementTypes.get(ruleMarker);
			marker.done(iElementType);
			markers.remove(ruleMarker);
			iElementTypes.remove(ruleMarker);
		}
	}

	protected final void markRule(IElementType iElementType) {
		ruleIElementTypes.push(iElementType);
		ruleMarkers.push(psiBuilder.mark());
	}

	protected final void doneRule() {
		IElementType iElementType = ruleIElementTypes.pop();
		ruleMarkers.pop().done(iElementType);
	}

	public final boolean appendErrorMessage() {
		if (errorMessage == null) {
			return false;
		}
		Marker error = psiBuilder.mark();
		input.consume();
		error.error(errorMessage);
		errorMessage = null;
		return true;
	}

	/**
	 * Match current input symbol against ttype. Attempt single token insertion
	 * or deletion error recovery. If that fails, throw
	 * MismatchedTokenException.
	 * 
	 * To turn off single token insertion or deletion error recovery, override
	 * recoverFromMismatchedToken() and have it throw an exception. See
	 * TreeParser.recoverFromMismatchedToken(). This way any error in a rule
	 * will cause an exception and immediate exit from rule. Rule would recover
	 * by resynchronizing to the set of symbols that can follow rule ref.
	 */
	public Object match(IntStream input, int ttype, BitSet follow)
			throws RecognitionException {
		// System.out.println("match "+((TokenStream)input).LT(1));
		Object matchedSymbol = getCurrentInputSymbol(input);
		if (input.LA(1) == ttype) {
			if (!appendErrorMessage()) {
				input.consume();
			}
			state.errorRecovery = false;
			state.failed = false;
			return matchedSymbol;
		}
		if (state.backtracking > 0) {
			state.failed = true;
			return matchedSymbol;
		}
		matchedSymbol = recoverFromMismatchedToken(input, ttype, follow);
		return matchedSymbol;
	}

	protected Object recoverFromMismatchedToken(IntStream input, int ttype,
			BitSet follow) throws RecognitionException {
		RecognitionException e = null;
		// if next token is what we are looking for then "delete" this token
		if (mismatchIsUnwantedToken(input, ttype)) {
			e = new UnwantedTokenException(ttype, input);
			/*
			 * System.err.println("recoverFromMismatchedToken deleting "+
			 * ((TokenStream)input).LT(1)+
			 * " since "+((TokenStream)input).LT(2)+" is what we want");
			 */
			beginResync();
			markError();
			input.consume(); // simply delete extra token
			doneError(e);
			endResync();
			reportError(e); // report after consuming so AW sees the token in
							// the exception
			// we want to return the token we're actually matching
			Object matchedSymbol = getCurrentInputSymbol(input);
			input.consume(); // move past ttype token as if all were ok
			return matchedSymbol;
		}
		// can't recover with single token deletion, try insertion
		if (mismatchIsMissingToken(input, follow)) {
			Object inserted = getMissingSymbol(input, e, ttype, follow);
			e = new MissingTokenException(ttype, input, inserted);
			errorMessage = getErrorMessage(e, getTokenNames());
			reportError(e); // report after inserting so AW sees the token in
							// the exception
			return inserted;
		}
		// even that didn't work; must throw the exception
		e = new MismatchedTokenException(ttype, input);
		throw e;
	}

	protected final void markError() {
		if (ruleMarkers.isEmpty()) {
			errorMarker = psiBuilder.mark();
			return;
		}
		Marker ruleMarker = ruleMarkers.peek();
		Marker marker = markers.get(ruleMarker);
		if (marker != null) {
			errorMarker = marker;
			markers.remove(ruleMarker);
			iElementTypes.remove(ruleMarker);
		} else {
			errorMarker = psiBuilder.mark();
		}
	}

	protected final void doneError(RecognitionException re) {
		String errorMessage = getErrorMessage(re, getTokenNames());
		errorMarker.error(errorMessage);
		errorMarker = null;
	}

	@Override
	public String getSourceName() {
		return input.getSourceName();
	}

}
