package com.atlassian.rstocker.cm.internal;

import com.atlassian.rstocker.cm.node.Block;
import com.atlassian.rstocker.cm.node.Node;
import com.atlassian.rstocker.cm.node.Paragraph;

public class ParagraphParser extends AbstractBlockParser {

	private final Paragraph block = new Paragraph();
	// TODO: Can this be inlined?
	private BlockContent content = new BlockContent();

	@Override
	public ContinueResult parseLine(String line, int nextNonSpace, int[] offset, boolean blank) {
		return blank ? ContinueResult.NOT_MATCHED : ContinueResult.MATCHED;
	}

	@Override
	public boolean acceptsLine() {
		return true;
	}

	@Override
	public void addLine(String line) {
		content.add(line);
	}

	@Override
	public void finalizeBlock(InlineParser inlineParser) {
		int pos;
		String contentString = content.getString();

		// try parsing the beginning as link reference definitions:
		while (contentString.charAt(0) == '[' &&
				(pos = inlineParser.parseReference(contentString)) != 0) {
			contentString = contentString.substring(pos);
			if (DocumentParser.isBlank(contentString)) {
				block.unlink();
				// TODO: Return something so that inlines aren't processed here?
				break;
			}
		}
		content = new BlockContent(contentString);
	}

	@Override
	public void processInlines(InlineParser inlineParser) {
		inlineParser.parse(block, content.getString());
	}

	@Override
	public boolean canContain(Node.Type type) {
		return false;
	}

	@Override
	public Block getBlock() {
		return block;
	}

	public boolean hasSingleLine() {
		return content.hasSingleLine();
	}

	public boolean hasLines() {
		return content.hasLines();
	}
}