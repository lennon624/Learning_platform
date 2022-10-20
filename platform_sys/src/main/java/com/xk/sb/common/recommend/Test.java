package com.xk.sb.common.recommend;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author WinUser
 */
public class Test {
    public static void main(String[] args) throws TasteException, SQLException, IOException {
        // 基于物品的推荐
        ItemBased itemBased = new ItemBased();
        List<RecommendedItem> itemBasedList = itemBased.getRecommendedItemList("10", 5);
        if (itemBasedList.size() > 0) {
            for (RecommendedItem recommendedItem : itemBasedList) {
                System.out.println(recommendedItem.getItemID());
            }
        }

        // 基于用户的推荐
        UserBased userBased = new UserBased();
        List<RecommendedItem> userBasedList = userBased.getRecommendedItemList("10", 5);
        if (userBasedList.size() > 0) {
            for (RecommendedItem recommendedItem : userBasedList) {
                System.out.println(recommendedItem.getItemID());
            }
        }
    }
}
