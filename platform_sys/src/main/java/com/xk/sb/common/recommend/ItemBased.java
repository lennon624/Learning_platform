package com.xk.sb.common.recommend;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Dover
 */
public class ItemBased {
    public List<RecommendedItem> getRecommendedItemList(String userId, int howMany) throws TasteException, SQLException {

        // 利用ReloadFromJDBCDataModel包裹jdbcDataModel,可以把输入加入内存计算，加快计算速度。
        ReloadFromJDBCDataModel dataModel = new ReloadFromJDBCDataModel(JDBCDataModelUtil.getDataModel());

        // 使用 欧几里得空间距离相似度计算方式
        ItemSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
        // 使用 推荐器
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, similarity);
        // 获取用户id为userId的推荐
        List<RecommendedItem> recommendedItemList = recommender.recommend(Long.parseLong(userId), howMany);

        return recommendedItemList;
    }
}
