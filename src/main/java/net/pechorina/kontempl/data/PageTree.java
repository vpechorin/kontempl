package net.pechorina.kontempl.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageTree extends GenericTree<Page> {
	static final Logger logger = LoggerFactory.getLogger(PageTree.class);
    
    public void updateItemsCounters(Map<Long, Integer> pageItems) {
        for (GenericTreeNode<Page> rootPage : this.getChildren() ) {
            if (rootPage.hasChildren()) {
                int itemsInSubcategories = getItemsInSubcategories(rootPage);
                rootPage.setItems(itemsInSubcategories);
            }
        }
    }
    
    public int getItemsInSubcategories(GenericTreeNode<Page> page) {
        int items = 0;
        for (GenericTreeNode<Page> p : page.getChildren() ) {
            if (p.hasChildren()) {
                int n = getItemsInSubcategories(p);
                p.setItems(n);
                items += n;
            }
            else {
                items += p.getItems();
            }
        }
        return items;
    }
    
    public GenericTreeNode<Page> findPageNode(String name) {
        GenericTreeNode<Page> n = new GenericTreeNode<Page>();
        List<GenericTreeNode<Page>> children = this.listAllChildren();
        // logger.debug("Found " + children.size() + " children. Treesize: " + this.getNumberOfNodes());
        for (GenericTreeNode<Page> node : children) {
            // logger.debug("Category:" + node.getData().getCid() + " / " + node.getData().getName());
            if (node.getData().getName().equalsIgnoreCase(name)) {
                n = node;
                // logger.debug("Node found");
                break;
            }
        }

        return n;
    }

    public GenericTreeNode<Page> findPageNode(Integer id) {
        GenericTreeNode<Page> n = new GenericTreeNode<Page>();
        List<GenericTreeNode<Page>> children = this.listAllChildren();
        // logger.debug("Found " + children.size() + " children. Treesize: " + this.getNumberOfNodes());
        for (GenericTreeNode<Page> node : children) {
            if (node.getData().getId().equals(id)) {
                n = node;
                break;
            }
        }

        return n;
    }    
    
    public List<Page> traceCategoryParents(String name) {
        GenericTreeNode<Page> n = findPageNode(name);
        List<Page> l = new ArrayList<Page>();
        if (n != null) {
            List<GenericTreeNode<Page>> pageNodes = n.traceParents();
            for (int i = (pageNodes.size() - 1); i >= 0; i--) {
                GenericTreeNode<Page> pn = pageNodes.get(i);
                l.add(pn.getData());
            }
        }
        return l;
    }
    
    public boolean isTheSameBranch(Integer pageId, Integer targetPageId) {
        // logger.debug("catCid:" + catCid + " targetCid: " + targetCid);
        if (pageId.equals(targetPageId)) {
            return true;
        }
        
        GenericTreeNode<Page> n = findPageNode(pageId);
        if (n != null) {
            List<GenericTreeNode<Page>> pageNodes = n.traceParents();
            for (int i = (pageNodes.size() - 1); i >= 0; i--) {
                GenericTreeNode<Page> pn = pageNodes.get(i);
                if (pn.getData().getId().equals(targetPageId) ) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("PageTree []");
    	String padding = "---- ";
    	if (this.hasChildren()) {
    		List<GenericTreeNode<Page>> children = this.getChildren();
    		for(GenericTreeNode<Page> n: children) {
    			builder.append(padding);
    			builder.append(n.getData().getId());
    			builder.append("/");
    			builder.append(n.getData().getName());
    			if (n.getParent() != null) {
    				builder.append(" [parent:" + n.getParent().getData().getId() + "]");
    			}
    			builder.append("\n");

    			auxPrintBranch(n, builder, padding);
    		}
    	}

    	return builder.toString();
    }
    
	private void auxPrintBranch(GenericTreeNode<Page> parent, StringBuilder builder, String margin) {
		String padding = "     ";
		if (parent.hasChildren()) {
			for(GenericTreeNode<Page> n: parent.getChildren()) {
				builder.append(margin);
				builder.append(padding);
				builder.append(n.getData().getId());
				builder.append("/");
				builder.append(n.getData().getName());
				if (n.getParent() != null) {
					builder.append(" [parent:" + n.getParent().getData().getId() + "]");
				}
				builder.append("\n");
				auxPrintBranch(n, builder, margin + padding);
			}
		}
	}
	
}
