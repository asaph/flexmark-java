package com.vladsch.flexmark.ext.gfm.issues.internal;

import com.vladsch.flexmark.ext.gfm.issues.GfmIssue;
import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.options.DataHolder;

import java.util.HashSet;
import java.util.Set;

public class GfmIssuesNodeRenderer implements NodeRenderer
        // , PhasedNodeRenderer
{
    private final GfmIssuesOptions options;

    public GfmIssuesNodeRenderer(DataHolder options) {
        this.options = new GfmIssuesOptions(options);
    }

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        Set<NodeRenderingHandler<?>> set = new HashSet<NodeRenderingHandler<?>>();
        // @formatter:off
        set.add(new NodeRenderingHandler<GfmIssue>(GfmIssue.class, new CustomNodeRenderer<GfmIssue>() { @Override public void render(GfmIssue node, NodeRendererContext context, HtmlWriter html) { GfmIssuesNodeRenderer.this.render(node, context, html); } }));
        // @formatter:on
        return set;
    }

    private void render(GfmIssue node, NodeRendererContext context, HtmlWriter html) {
        if (context.isDoNotRenderLinks()) {
            html.text(node.getChars());
        } else {
            StringBuilder sb = new StringBuilder();

            sb.append(options.gitHubIssuesUrlRoot).append(options.gitHubIssueUrlPrefix).append(node.getText()).append(options.gitHubIssueUrlSuffix);

            html.srcPos(node.getChars()).attr("href", sb.toString()).withAttr().tag("a");
            html.raw(options.gitHubIssueTextPrefix);
            html.text(node.getChars());
            html.raw(options.gitHubIssueTextSuffix);
            html.tag("/a");
        }
    }

    public static class Factory implements NodeRendererFactory {
        @Override
        public NodeRenderer create(final DataHolder options) {
            return new GfmIssuesNodeRenderer(options);
        }
    }
}
