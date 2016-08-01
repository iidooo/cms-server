package com.iidooo.cms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.util.NewBeanInstanceStrategy;

import org.apache.log4j.Logger;

import com.iidooo.cms.model.po.CmsChannel;
import com.iidooo.core.model.vo.TreeNode;

public class ChannelUtil {

    private static final Logger logger = Logger.getLogger(ChannelUtil.class);

    public static List<TreeNode> constructChannelTree(List<CmsChannel> channelList) {
        List<TreeNode> result = new ArrayList<TreeNode>();
        try {
            // Key: channelID
            // Value: CmsChannel
            Map<Integer, CmsChannel> channelMap = new HashMap<Integer, CmsChannel>();
            // Key: channelID
            // Value: TreeNode
            Map<Integer, TreeNode> treeNodeMap = new HashMap<Integer, TreeNode>();
            
            TreeNode root = new TreeNode();
            CmsChannel rootChannel = new CmsChannel();
            rootChannel.setChannelID(0);
            root.setText("root");
            root.setData(rootChannel);
            treeNodeMap.put(0, root);
            
            for (CmsChannel item : channelList) {
                channelMap.put(item.getChannelID(), item);

                TreeNode treeNode = new TreeNode();
                treeNode.setText(item.getChannelName());
                treeNode.setData(item);
                treeNodeMap.put(item.getChannelID(), treeNode);
            }

            for (Integer channelID : channelMap.keySet()) {
                CmsChannel channel = channelMap.get(channelID);
                TreeNode treeNode = treeNodeMap.get(channelID);

                TreeNode parentTreeNode = treeNodeMap.get(channel.getParentID());
                if (parentTreeNode != null) {
                    parentTreeNode.getNodes().add(treeNode);
                } 
            }
            
            result.add(root);

        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }
}
