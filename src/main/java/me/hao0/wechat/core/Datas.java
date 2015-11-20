package me.hao0.wechat.core;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import me.hao0.wechat.model.data.article.articleSummaryDaily;
import me.hao0.wechat.model.data.article.ArticleShare;
import me.hao0.wechat.model.data.article.ArticleShareHour;
import me.hao0.wechat.model.data.article.ArticleSummary;
import me.hao0.wechat.model.data.article.ArticleSummaryHour;
import me.hao0.wechat.model.data.article.ArticleTotal;
import me.hao0.wechat.model.data.interfaces.InterfaceSummary;
import me.hao0.wechat.model.data.interfaces.InterfaceSummaryHour;
import me.hao0.wechat.model.data.msg.MsgSendDist;
import me.hao0.wechat.model.data.msg.MsgSendSummary;
import me.hao0.wechat.model.data.msg.MsgSendSummaryHour;
import me.hao0.wechat.model.data.user.UserCumulate;
import me.hao0.wechat.model.data.user.UserSummary;
import me.hao0.wechat.utils.Jsons;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 */
public class Datas extends Component {

    /**
     * 获取用户增减数据
     */
    private static final String USER_SUMMARY = "https://api.weixin.qq.com/datacube/getusersummary?access_token=";

    /**
     * 获取累计用户数据
     */
    private static final String USER_CUMULATE = "https://api.weixin.qq.com/datacube/getusercumulate?access_token=";

    /**
     * 图文群发每日数据
     */
    private static final String ARTICLE_DAILY_SUMMARY = "https://api.weixin.qq.com/datacube/getarticlesummary?access_token=";

    /**
     * 图文群发总数据
     */
    private static final String ARTICLE_TOTAL = "https://api.weixin.qq.com/datacube/getarticletotal?access_token=";

    /**
     * 图文统计数据
     */
    private static final String ARTICLE_SUMMARY = "https://api.weixin.qq.com/datacube/getuserread?access_token=";

    /**
     * 图文统计分时数据
     */
    private static final String ARTICLE_SUMMARY_HOUR = "https://api.weixin.qq.com/datacube/getuserreadhour?access_token=";

    /**
     * 图文分享转发数据
     */
    private static final String ARTICLE_SHARE = "https://api.weixin.qq.com/datacube/getusershare?access_token=";

    /**
     * 图文分享转发分时数据
     */
    private static final String ARTICLE_SHARE_HOUR = "https://api.weixin.qq.com/datacube/getusersharehour?access_token=";

    /**
     * 接口分析数据
     */
    private static final String INTERFACE_SUMMARY = "https://api.weixin.qq.com/datacube/getinterfacesummary?access_token=";

    /**
     * 接口分析分时数据
     */
    private static final String INTERFACE_SUMMARY_HOUR = "https://api.weixin.qq.com/datacube/getinterfacesummaryhour?access_token=";

    /**
     * 消息发送数据
     */
    private static final String MSG_SEND_SUMMARY = "https://api.weixin.qq.com/datacube/getupstreammsg?access_token=";

    /**
     * 消息发送分时数据
     */
    private static final String MSG_SEND_SUMMARY_HOUR = "https://api.weixin.qq.com/datacube/getupstreammsghour?access_token=";

    /**
     * 消息发送周数据
     */
    private static final String MSG_SEND_SUMMARY_WEEK = "https://api.weixin.qq.com/datacube/getupstreammsgweek?access_token=";

    /**
     * 消息发送月数据
     */
    private static final String MSG_SEND_SUMMARY_MONTH = "https://api.weixin.qq.com/datacube/getupstreammsgmonth?access_token=";

    /**
     * 消息发送分布周数据
     */
    private static final String MSG_SEND_DIST = "https://api.weixin.qq.com/datacube/getupstreammsgdist?access_token=";

    /**
     * 消息发送分布周数据
     */
    private static final String MSG_SEND_SUMMARY_DIST_WEEK = "https://api.weixin.qq.com/datacube/getupstreammsgdistweek?access_token=";

    /**
     * 消息发送分布周数据
     */
    private static final String MSG_SEND_SUMMARY_DIST_MONTH = "https://api.weixin.qq.com/datacube/getupstreammsgdistmonth?access_token=";

    private static final JavaType USER_SUMMARY_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, UserSummary.class);

    private static final JavaType USER_CUMULATE_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, UserCumulate.class);

    private static final JavaType ARTICLE_DAILY_SUMMARY_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, articleSummaryDaily.class);

    private static final JavaType ARTICLE_TOTAL_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, ArticleTotal.class);

    private static final JavaType ARTICLE_SUMMARY_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, ArticleSummary.class);

    private static final JavaType ARTICLE_SUMMARY_HOUR_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, ArticleSummaryHour.class);

    private static final JavaType ARTICLE_SHARE_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, ArticleShare.class);

    private static final JavaType ARTICLE_SHARE_HOUR_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, ArticleShareHour.class);

    private static final JavaType INTERFACE_SUMMARY_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, InterfaceSummary.class);

    private static final JavaType INTERFACE_SUMMARY_HOUR_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, InterfaceSummaryHour.class);

    private static final JavaType MSG_SEND_SUMMARY_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, MsgSendSummary.class);

    private static final JavaType MSG_SEND_SUMMARY_HOUR_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, MsgSendSummaryHour.class);

    private static final JavaType MSG_SEND_DIST_LIST_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, MsgSendDist.class);

    Datas(){}

    /**
     * 查询用户增量数据(最多跨度7天，endDate - startDate < 7)
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 用户增量统计
     */
    public List<UserSummary> userSummary(String startDate, String endDate){
        return userSummary(loadAccessToken(), startDate, endDate);
    }

    /**
     * 查询用户增量数据(最多跨度7天，endDate - startDate < 7)
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void userSummary(final String startDate, final String endDate, Callback<List<UserSummary>> cb){
        userSummary(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 查询用户增量数据(最多跨度7天，endDate - startDate < 7)
     * @param accessToken accessToken
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void userSummary(final String accessToken, final String startDate, final String endDate, Callback<List<UserSummary>> cb){
        doAsync(new AsyncFunction<List<UserSummary>>(cb) {
            @Override
            public List<UserSummary> execute() throws Exception {
                return userSummary(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 查询用户增量数据(最多跨度7天，endDate - startDate < 7)
     * @param accessToken accessToken
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 用户增量统计
     */
    public List<UserSummary> userSummary(String accessToken, String startDate, String endDate){
        return doSummary(USER_SUMMARY + accessToken, startDate, endDate, USER_SUMMARY_LIST_TYPE);
    }

    /**
     * 查询用户总量数据(最多跨度7天，endDate - startDate < 7)
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 用户增量统计
     */
    public List<UserCumulate> userCumulate(String startDate, String endDate){
        return userCumulate(loadAccessToken(), startDate, endDate);
    }

    /**
     * 查询用户总量数据(最多跨度7天，endDate - startDate < 7)
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void userCumulate(final String startDate, final String endDate, Callback<List<UserCumulate>> cb){
        userCumulate(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 查询用户总量数据(最多跨度7天，endDate - startDate < 7)
     * @param accessToken accessToken
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void userCumulate(final String accessToken, final String startDate, final String endDate, Callback<List<UserCumulate>> cb){
        doAsync(new AsyncFunction<List<UserCumulate>>(cb) {
            @Override
            public List<UserCumulate> execute() throws Exception {
                return userCumulate(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 查询用户总量数据(最多跨度7天，endDate - startDate < 7)
     * @param accessToken accessToken
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 用户增量统计
     */
    public List<UserCumulate> userCumulate(String accessToken, String startDate, String endDate){
        return doSummary(USER_CUMULATE + accessToken, startDate, endDate, USER_CUMULATE_LIST_TYPE);
    }

    /**
     * 获取图文群发每日数据:
     *  某天所有被阅读过的文章（仅包括群发的文章）在当天的阅读次数等数据
     * @param date 日期
     * @return 图文群发每日数据
     */
    public List<articleSummaryDaily> articleSummaryDaily(String date){
        return articleSummaryDaily(loadAccessToken(), date);
    }

    /**
     * 获取图文群发每日数据:
     *  某天所有被阅读过的文章（仅包括群发的文章）在当天的阅读次数等数据
     * @param accessToken accessToken
     * @param date 日期
     * @param cb 回调
     */
    public void articleSummaryDaily(final String accessToken, final String date, Callback<List<articleSummaryDaily>> cb){
        doAsync(new AsyncFunction<List<articleSummaryDaily>>(cb) {
            @Override
            public List<articleSummaryDaily> execute() throws Exception {
                return articleSummaryDaily(accessToken, date);
            }
        });
    }

    /**
     * 获取图文群发每日数据:
     *  某天所有被阅读过的文章（仅包括群发的文章）在当天的阅读次数等数据
     * @param date 日期
     * @param cb 回调
     */
    public void articleSummaryDaily(final String date, Callback<List<articleSummaryDaily>> cb){
        articleSummaryDaily(loadAccessToken(), date, cb);
    }

    /**
     * 获取图文群发每日数据:
     *  某天所有被阅读过的文章（仅包括群发的文章）在当天的阅读次数等数据
     * @param accessToken accessToken
     * @param date 日期
     * @return 图文群发每日数据
     */
    public List<articleSummaryDaily> articleSummaryDaily(String accessToken, String date){
        return doSummary(ARTICLE_DAILY_SUMMARY + accessToken, date, date, ARTICLE_DAILY_SUMMARY_LIST_TYPE);
    }

    /**
     * 获取图文群发总数据
     * @param date 日期
     * @return 图文群发总数据
     */
    public List<ArticleTotal> articleTotal(String date){
        return articleTotal(loadAccessToken(), date);
    }

    /**
     * 获取图文群发总数据
     * @param date 日期
     * @param cb 回调
     */
    public void articleTotal(final String date, Callback<List<ArticleTotal>> cb){
        articleTotal(loadAccessToken(), date, cb);
    }

    /**
     * 获取图文群发总数据
     * @param accessToken accessToken
     * @param date 日期
     * @param cb 回调
     */
    public void articleTotal(final String accessToken, final String date, Callback<List<ArticleTotal>> cb){
        doAsync(new AsyncFunction<List<ArticleTotal>>(cb) {
            @Override
            public List<ArticleTotal> execute() throws Exception {
                return articleTotal(accessToken, date);
            }
        });
    }

    /**
     * 获取图文群发总数据
     * @param accessToken accessToken
     * @param date 日期
     * @return 图文群发总数据
     */
    public List<ArticleTotal> articleTotal(String accessToken, String date){
        return doSummary(ARTICLE_TOTAL + accessToken, date, date, ARTICLE_TOTAL_LIST_TYPE);
    }

    /**
     * 获取图文统计数据(最多跨度3天，endDate - startDate < 3)
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void articleSummary(final String startDate, final String endDate, Callback<List<ArticleSummary>> cb){
        articleSummary(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取图文统计数据(最多跨度3天，endDate - startDate < 3)
     * @param accessToken accessToken
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void articleSummary(final String accessToken, final String startDate, final String endDate, Callback<List<ArticleSummary>> cb){
        doAsync(new AsyncFunction<List<ArticleSummary>>(cb) {
            @Override
            public List<ArticleSummary> execute() throws Exception {
                return articleSummary(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取图文统计数据(最多跨度3天，endDate - startDate < 3)
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 图文统计数据
     */
    public List<ArticleSummary> articleSummary(String startDate, String endDate){
        return articleSummary(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取图文统计数据(最多跨度3天，endDate - startDate < 3)
     * @param accessToken accessToken
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 图文统计数据
     */
    public List<ArticleSummary> articleSummary(String accessToken, String startDate, String endDate){
        return doSummary(ARTICLE_SUMMARY + accessToken, startDate, endDate, ARTICLE_SUMMARY_LIST_TYPE);
    }

    /**
     * 获取图文统计数据
     * @param date 日期
     * @param cb 回调
     */
    public void articleSummaryHourly(final String date, Callback<List<ArticleSummaryHour>> cb){
        articleSummaryHourly(loadAccessToken(), date, cb);
    }

    /**
     * 获取图文统计数据
     * @param accessToken accessToken
     * @param date 日期
     * @param cb 回调
     */
    public void articleSummaryHourly(final String accessToken, final String date, Callback<List<ArticleSummaryHour>> cb){
        doAsync(new AsyncFunction<List<ArticleSummaryHour>>(cb) {
            @Override
            public List<ArticleSummaryHour> execute() throws Exception {
                return articleSummaryHourly(accessToken, date);
            }
        });
    }

    /**
     * 获取图文统计数据
     * @param date 日期
     * @return 图文统计分时数据
     */
    public List<ArticleSummaryHour> articleSummaryHourly(String date){
        return articleSummaryHourly(loadAccessToken(), date);
    }

    /**
     * 获取图文统计数据
     * @param accessToken accessToken
     * @param date 日期
     * @return 图文统计分时数据
     */
    public List<ArticleSummaryHour> articleSummaryHourly(String accessToken, String date){
        return doSummary(ARTICLE_SUMMARY_HOUR + accessToken, date, date, ARTICLE_SUMMARY_HOUR_LIST_TYPE);
    }

    /**
     * 获取图文分享转发数据(最多跨度7天，endDate - startDate < 7)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void articleShare(final String startDate, final String endDate, Callback<List<ArticleShare>> cb){
        articleShare(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取图文分享转发数据(最多跨度7天，endDate - startDate < 7)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void articleShare(final String accessToken, final String startDate, final String endDate, Callback<List<ArticleShare>> cb){
        doAsync(new AsyncFunction<List<ArticleShare>>(cb) {
            @Override
            public List<ArticleShare> execute() throws Exception {
                return articleShare(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取图文分享转发数据(最多跨度7天，endDate - startDate < 7)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 图文分享转发数据
     */
    public List<ArticleShare> articleShare(String startDate, String endDate){
        return articleShare(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取图文分享转发数据(最多跨度7天，endDate - startDate < 7)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 图文分享转发数据
     */
    public List<ArticleShare> articleShare(String accessToken, String startDate, String endDate){
        return doSummary(ARTICLE_SHARE + accessToken, startDate, endDate, ARTICLE_SHARE_LIST_TYPE);
    }

    /**
     * 获取图文分享转发分时数据
     * @param date 日期
     * @param cb 回调
     */
    public void articleShareByHourly(final String date, Callback<List<ArticleShareHour>> cb){
        articleShareByHourly(loadAccessToken(), date, cb);
    }

    /**
     * 获取图文分享转发分时数据
     * @param accessToken accessToken
     * @param date 日期
     * @param cb 回调
     */
    public void articleShareByHourly(final String accessToken, final String date, Callback<List<ArticleShareHour>> cb){
        doAsync(new AsyncFunction<List<ArticleShareHour>>(cb) {
            @Override
            public List<ArticleShareHour> execute() throws Exception {
                return articleShareByHourly(accessToken, date);
            }
        });
    }

    /**
     * 获取图文分享转发分时数据
     * @param date 日期
     * @return 图文分享转发分时数据
     */
    public List<ArticleShareHour> articleShareByHourly(String date){
        return articleShareByHourly(loadAccessToken(), date);
    }

    /**
     * 获取图文分享转发分时数据
     * @param accessToken accessToken
     * @param date 日期
     * @return 图文分享转发分时数据
     */
    public List<ArticleShareHour> articleShareByHourly(String accessToken, String date){
        return doSummary(ARTICLE_SHARE_HOUR + accessToken, date, date, ARTICLE_SHARE_HOUR_LIST_TYPE);
    }

    /**
     * 获取接口分析数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void interfaceSummary(final String startDate, final String endDate, Callback< List<InterfaceSummary>> cb){
        interfaceSummary(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取接口分析数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void interfaceSummary(final String accessToken, final String startDate, final String endDate, Callback< List<InterfaceSummary>> cb){
        doAsync(new AsyncFunction<List<InterfaceSummary>>(cb) {
            @Override
            public List<InterfaceSummary> execute() throws Exception {
                return interfaceSummary(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取接口分析数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 接口分析数据
     */
    public List<InterfaceSummary> interfaceSummary(String startDate, String endDate){
        return interfaceSummary(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取接口分析数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 接口分析数据
     */
    public List<InterfaceSummary> interfaceSummary(String accessToken, String startDate, String endDate){
        return doSummary(INTERFACE_SUMMARY + accessToken, startDate, endDate, INTERFACE_SUMMARY_LIST_TYPE);
    }

    /**
     * 获取接口分析分时数据
     * @param date 日期
     * @param cb 回调
     */
    public void interfaceSummaryHourly(final String date, Callback<List<InterfaceSummaryHour>> cb){
        interfaceSummaryHourly(loadAccessToken(), date, cb);
    }

    /**
     * 获取接口分析分时数据
     * @param accessToken accessToken
     * @param date 日期
     * @param cb 回调
     */
    public void interfaceSummaryHourly(final String accessToken, final String date, Callback<List<InterfaceSummaryHour>> cb){
        doAsync(new AsyncFunction<List<InterfaceSummaryHour>>(cb) {
            @Override
            public List<InterfaceSummaryHour> execute() throws Exception {
                return interfaceSummaryHourly(accessToken, date);
            }
        });
    }

    /**
     * 获取接口分析分时数据
     * @param date 日期
     * @return 接口分析分时数据
     */
    public List<InterfaceSummaryHour> interfaceSummaryHourly(String date){
        return interfaceSummaryHourly(loadAccessToken(), date);
    }

    /**
     * 获取接口分析分时数据
     * @param accessToken accessToken
     * @param date 日期
     * @return 接口分析分时数据
     */
    public List<InterfaceSummaryHour> interfaceSummaryHourly(String accessToken, String date){
        return doSummary(INTERFACE_SUMMARY_HOUR + accessToken, date, date, INTERFACE_SUMMARY_HOUR_LIST_TYPE);
    }

    /**
     * 获取消息分析数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendSummary(final String startDate, final String endDate, Callback<List<MsgSendSummary>> cb){
        msgSendSummary(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取消息分析数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendSummary(final String accessToken, final String startDate, final String endDate, Callback<List<MsgSendSummary>> cb){
        doAsync(new AsyncFunction<List<MsgSendSummary>>(cb) {
            @Override
            public List<MsgSendSummary> execute() throws Exception {
                return msgSendSummary(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取消息分析数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 消息分析数据
     */
    public List<MsgSendSummary> msgSendSummary(String startDate, String endDate){
        return msgSendSummary(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取消息分析数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 消息分析数据
     */
    public List<MsgSendSummary> msgSendSummary(String accessToken, String startDate, String endDate){
        return doSummary(MSG_SEND_SUMMARY + accessToken, startDate, endDate, MSG_SEND_SUMMARY_LIST_TYPE);
    }

    /**
     * 获取消息分析分时数据
     * @param date 日期
     * @param cb 回调
     */
    public void msgSendSummaryHourly(final String date, Callback<List<MsgSendSummaryHour>> cb){
        msgSendSummaryHourly(loadAccessToken(), date, cb);
    }

    /**
     * 获取消息分析分时数据
     * @param accessToken accessToken
     * @param date 日期
     * @param cb 回调
     */
    public void msgSendSummaryHourly(final String accessToken, final String date, Callback<List<MsgSendSummaryHour>> cb){
        doAsync(new AsyncFunction<List<MsgSendSummaryHour>>(cb) {
            @Override
            public List<MsgSendSummaryHour> execute() throws Exception {
                return msgSendSummaryHourly(accessToken, date);
            }
        });
    }

    /**
     * 获取消息分析分时数据
     * @param date 日期
     * @return 消息分析分时数据
     */
    public List<MsgSendSummaryHour> msgSendSummaryHourly(String date){
        return msgSendSummaryHourly(loadAccessToken(), date);
    }

    /**
     * 获取消息分析分时数据
     * @param accessToken accessToken
     * @param date 日期
     * @return 消息分析分时数据
     */
    public List<MsgSendSummaryHour> msgSendSummaryHourly(String accessToken, String date){
        return doSummary(MSG_SEND_SUMMARY_HOUR + accessToken, date, date, MSG_SEND_SUMMARY_HOUR_LIST_TYPE);
    }

    /**
     * 获取消息分析周数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendSummaryWeekly(final String startDate, final String endDate, Callback<List<MsgSendSummary>> cb){
        msgSendSummaryWeekly(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取消息分析周数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendSummaryWeekly(final String accessToken, final String startDate, final String endDate, Callback<List<MsgSendSummary>> cb){
        doAsync(new AsyncFunction<List<MsgSendSummary>>(cb) {
            @Override
            public List<MsgSendSummary> execute() throws Exception {
                return msgSendSummary(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取消息分析周数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 消息分析数据
     */
    public List<MsgSendSummary> msgSendSummaryWeekly(String startDate, String endDate){
        return msgSendSummaryWeekly(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取消息分析周数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 消息分析数据
     */
    public List<MsgSendSummary> msgSendSummaryWeekly(String accessToken, String startDate, String endDate){
        return doSummary(MSG_SEND_SUMMARY_WEEK + accessToken, startDate, endDate, MSG_SEND_SUMMARY_LIST_TYPE);
    }

    /**
     * 获取消息分析月数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendSummaryMonthly(final String startDate, final String endDate, Callback<List<MsgSendSummary>> cb){
        msgSendSummaryMonthly(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取消息分析月数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendSummaryMonthly(final String accessToken, final String startDate, final String endDate, Callback<List<MsgSendSummary>> cb){
        doAsync(new AsyncFunction<List<MsgSendSummary>>(cb) {
            @Override
            public List<MsgSendSummary> execute() throws Exception {
                return msgSendSummaryMonthly(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取消息分析月数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 消息分析数据
     */
    public List<MsgSendSummary> msgSendSummaryMonthly(String startDate, String endDate){
        return msgSendSummaryMonthly(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取消息分析月数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 消息分析数据
     */
    public List<MsgSendSummary> msgSendSummaryMonthly(String accessToken, String startDate, String endDate){
        return doSummary(MSG_SEND_SUMMARY_MONTH + accessToken, startDate, endDate, MSG_SEND_SUMMARY_LIST_TYPE);
    }

    /**
     * 获取消息发送分布数据(最多跨度15天，endDate - startDate < 15)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendDist(final String accessToken, final String startDate, final String endDate, Callback<List<MsgSendDist>> cb){
        doAsync(new AsyncFunction<List<MsgSendDist>>(cb) {
            @Override
            public List<MsgSendDist> execute() throws Exception {
                return msgSendDist(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取消息发送分布数据(最多跨度15天，endDate - startDate < 15)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendDist(final String startDate, final String endDate, Callback<List<MsgSendDist>> cb){
        msgSendDist(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取消息发送分布数据(最多跨度15天，endDate - startDate < 15)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 发送消息分布周数据
     */
    public List<MsgSendDist> msgSendDist(String startDate, String endDate){
        return msgSendDist(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取消息发送分布数据(最多跨度15天，endDate - startDate < 15)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 发送消息分布周数据
     */
    public List<MsgSendDist> msgSendDist(String accessToken, String startDate, String endDate){
        return doSummary(MSG_SEND_DIST + accessToken, startDate, endDate, MSG_SEND_DIST_LIST_TYPE);
    }

    /**
     * 获取消息发送分布周数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 发送消息分布周数据
     */
    public List<MsgSendDist> msgSendDistWeekly(String startDate, String endDate){
        return msgSendDistWeekly(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取消息发送分布周数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendDistWeekly(final String startDate, final String endDate, Callback<List<MsgSendDist>> cb){
        msgSendDistWeekly(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取消息发送分布周数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendDistWeekly(final String accessToken, final String startDate, final String endDate, Callback<List<MsgSendDist>> cb){
        doAsync(new AsyncFunction<List<MsgSendDist>>(cb) {
            @Override
            public List<MsgSendDist> execute() throws Exception {
                return msgSendDistWeekly(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取消息发送分布周数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 发送消息分布周数据
     */
    public List<MsgSendDist> msgSendDistWeekly(String accessToken, String startDate, String endDate){
        return doSummary(MSG_SEND_SUMMARY_DIST_WEEK + accessToken, startDate, endDate, MSG_SEND_DIST_LIST_TYPE);
    }

    /**
     * 获取消息发送分布月数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 发送消息分布月数据
     */
    public List<MsgSendDist> msgSendDistMonthly(String startDate, String endDate){
        return msgSendDistMonthly(loadAccessToken(), startDate, endDate);
    }

    /**
     * 获取消息发送分布月数据(最多跨度30天，endDate - startDate < 30)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendDistMonthly(final String startDate, final String endDate, Callback<List<MsgSendDist>> cb){
        msgSendDistMonthly(loadAccessToken(), startDate, endDate, cb);
    }

    /**
     * 获取消息发送分布月数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param cb 回调
     */
    public void msgSendDistMonthly(final String accessToken, final String startDate, final String endDate, Callback<List<MsgSendDist>> cb){
        doAsync(new AsyncFunction<List<MsgSendDist>>(cb) {
            @Override
            public List<MsgSendDist> execute() throws Exception {
                return msgSendDistMonthly(accessToken, startDate, endDate);
            }
        });
    }

    /**
     * 获取消息发送分布月数据(最多跨度30天，endDate - startDate < 30)
     * @param accessToken accessToken
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 发送消息分布月数据
     */
    public List<MsgSendDist> msgSendDistMonthly(String accessToken, String startDate, String endDate){
        return doSummary(MSG_SEND_SUMMARY_DIST_MONTH + accessToken, startDate, endDate, MSG_SEND_DIST_LIST_TYPE);
    }

    private <T> List<T> doSummary(String url, String startDate, String endDate, JavaType type){
        Map<String, Object> params = buildDateRange(startDate, endDate);
        Map<String, Object> resp = doPost(url, params);
        return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp.get("list")), type);
    }

    private Map<String, Object> buildDateRange(String start, String end) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("begin_date", start);
        params.put("end_date", end);
        return params;
    }
}
