package jp.gr.java_conf.snake0394.loglook_android.proxy;


import com.google.common.base.Optional;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * レスポンスに含まれている情報を ContentListener に提供するオブジェクト
 *
 */
public interface ResponseMetaData {

    /**
     * レスポンスのステータスコードを取得します
     * @return レスポンスのステータスコード
     */
    int getStatus();

    /**
     * コンテントタイプを取得します
     * @return コンテントタイプ
     */
    String getContentType();

    /**
     * レスポンスヘッダを取得します
     * @return レスポンスヘッダのMap
     */
    @Deprecated
    Map<String, Collection<String>> getHeaders();

    /**
     * レスポンスに含まれるメッセージボディを返します
     * @return レスポンスに含まれるメッセージボディ
     */
    Optional<InputStream> getResponseBody();
}