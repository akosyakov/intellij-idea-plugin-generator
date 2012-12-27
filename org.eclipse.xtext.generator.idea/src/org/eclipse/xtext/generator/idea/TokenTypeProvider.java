package org.eclipse.xtext.generator.idea;

import com.intellij.psi.tree.IElementType;

public interface TokenTypeProvider {

    int getType(IElementType iElementType);

}
