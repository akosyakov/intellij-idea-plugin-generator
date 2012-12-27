package org.eclipse.xtext.generator.idea;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

public class PsiTokenStream implements TokenStream {

	private int markerIndex;

	private int lastMarkerIndex;

	private final PsiBuilder psiBuilder;

	private final TokenTypeProvider tokenTypeProvider;

	private final Map<Integer, PsiBuilder.Marker> markers;

	public PsiTokenStream(PsiBuilder psiBuilder,
			TokenTypeProvider tokenTypeProvider) {
		this.psiBuilder = psiBuilder;
		this.tokenTypeProvider = tokenTypeProvider;
		this.markers = new HashMap<Integer, PsiBuilder.Marker>();
	}

	public void consume() {
		psiBuilder.advanceLexer();
	}

	public int LA(int i) {
		return LT(i).getType();
	}

	public int index() {
		return LA(1);
	}

	/**
	 * Get Token at current input pointer + i ahead where i=1 is next Token. i<0
	 * indicates tokens in the past. So -1 is previous token and -2 is two
	 * tokens ago. LT(0) is undefined. For i>=n, return Token.EOFToken. Return
	 * null for LT(0) and any index that results in an absolute address that is
	 * negative.
	 */
	public Token LT(int k) {
		IElementType iElementType = psiBuilder.lookAhead(k - 1);
		if (iElementType == null) {
			return Token.EOF_TOKEN;
		}
		CommonToken commonToken = new CommonToken(
				tokenTypeProvider.getType(iElementType));
		commonToken.setText(psiBuilder.getTokenText());
		return commonToken;
	}

	public Token get(int i) {
		throw new UnsupportedOperationException();
	}

	public TokenSource getTokenSource() {
		throw new UnsupportedOperationException();
	}

	public String toString(int start, int stop) {
		throw new UnsupportedOperationException();
	}

	public String toString(Token start, Token stop) {
		throw new UnsupportedOperationException();
	}

	public int mark() {
		return mark(markerIndex++);
	}
	
	private int mark(int markerIndex) {
		markers.put(markerIndex, psiBuilder.mark());
		lastMarkerIndex = markerIndex;
		return markerIndex;
	}

	public void rewind(int index) {
		PsiBuilder.Marker marker = markers.remove(index);
		if (marker != null) {
			marker.rollbackTo();
		}
	}

	public void rewind() {
		rewind(lastMarkerIndex);
		mark(lastMarkerIndex);
	}

	public void release(int marker) {
		throw new UnsupportedOperationException();
	}

	public void seek(int index) {
		// Do nothing
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public String getSourceName() {
		throw new UnsupportedOperationException();
	}

}
