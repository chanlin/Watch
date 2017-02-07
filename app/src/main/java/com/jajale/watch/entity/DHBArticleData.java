package com.jajale.watch.entity;

import java.util.List;

/**
 * Created by llh on 16-4-29.
 */
public class DHBArticleData {


    /**
     * article_list : [{"article_id":"1","article_title":"风采1","article_praise_count":"10","article_img_url":"http://7xosde.com2.z0.glb.qiniucdn.com/dcb9b148-fabf-4ac2-b526-b6a2e635d37d.png"},{"article_id":"2","article_title":"风采2","article_praise_count":"10","article_img_url":"http://7xosde.com2.z0.glb.qiniucdn.com/dcb9b148-fabf-4ac2-b526-b6a2e635d37d.png"}]
     */

    private List<ArticleListEntity> article_list;

    public void setArticle_list(List<ArticleListEntity> article_list) {
        this.article_list = article_list;
    }

    public List<ArticleListEntity> getArticle_list() {
        return article_list;
    }

    public static class ArticleListEntity {
        /**
         * article_id : 1
         * article_title : 风采1
         * article_praise_count : 10
         * article_img_url : http://7xosde.com2.z0.glb.qiniucdn.com/dcb9b148-fabf-4ac2-b526-b6a2e635d37d.png
         */

        private String article_id;
        private String article_title;
        private String article_praise_count;
        private String article_img_url;

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public void setArticle_title(String article_title) {
            this.article_title = article_title;
        }

        public void setArticle_praise_count(String article_praise_count) {
            this.article_praise_count = article_praise_count;
        }

        public void setArticle_img_url(String article_img_url) {
            this.article_img_url = article_img_url;
        }

        public String getArticle_id() {
            return article_id;
        }

        public String getArticle_title() {
            return article_title;
        }

        public String getArticle_praise_count() {
            return article_praise_count;
        }

        public String getArticle_img_url() {
            return article_img_url;
        }
    }
}
