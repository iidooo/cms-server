package com.iidooo.cms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.iidooo.cms.model.po.CmsChannel;
import com.iidooo.core.model.vo.TreeNode;

public class ChannelUtil {

    private static final Logger logger = Logger.getLogger(ChannelUtil.class);

    public static List<TreeNode> constructChannelTree(List<CmsChannel> channelList) {
        List<TreeNode> result = new ArrayList<TreeNode>();
        try {
            // Key: channelID
            // Value: TreeNode
            Map<Integer, TreeNode> treeNodeMap = new HashMap<Integer, TreeNode>();
            
            for (CmsChannel item : channelList) {
                TreeNode treeNode = new TreeNode();
                treeNode.setText(item.getChannelName());
                treeNode.setData(item);
                treeNodeMap.put(item.getChannelID(), treeNode);
            }

            for (CmsChannel channel : channelList) {
                TreeNode treeNode = treeNodeMap.get(channel.getChannelID());

                TreeNode parentTreeNode = treeNodeMap.get(channel.getParentID());
                if (parentTreeNode != null) {
                    parentTreeNode.getNodes().add(treeNode);
                } else {                    
                    result.add(treeNode);
                }
            }

        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }
}
