/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.identity.sso.cas.cache;

import org.wso2.carbon.identity.application.authentication.framework.store.SessionDataStore;
import org.wso2.carbon.identity.application.common.cache.BaseCache;
import org.wso2.carbon.identity.application.common.cache.CacheEntry;
import org.wso2.carbon.identity.application.common.cache.CacheKey;

public class ServiceTicketCache extends BaseCache<CacheKey, CacheEntry> {

    private static final String SESSION_DATA_CACHE_NAME = "CASServiceTicketCache";
    private static volatile ServiceTicketCache instance;
    private boolean useCache = true;

    private ServiceTicketCache(String cacheName) {
        super(cacheName);
    }

    public static ServiceTicketCache getInstance() {
        if (instance == null) {
            synchronized (ServiceTicketCache.class) {

                if (instance == null) {
                    instance = new ServiceTicketCache(SESSION_DATA_CACHE_NAME);
                }
            }
        }
        return instance;
    }

    @Override
    public void addToCache(CacheKey key, CacheEntry entry) {
        if (useCache) {
            super.addToCache(key, entry);
        }
        String keyValue = ((ServiceTicketCacheKey) key).getSessionDataKey();
        SessionDataStore.getInstance().storeSessionData(keyValue, SESSION_DATA_CACHE_NAME, entry);
    }

    @Override
    public CacheEntry getValueFromCache(CacheKey key) {
        CacheEntry cacheEntry = null;
        if (useCache) {
            cacheEntry = super.getValueFromCache(key);
        }
        if (cacheEntry == null) {
            String keyValue = ((ServiceTicketCacheKey) key).getSessionDataKey();
            cacheEntry = (ServiceTicketCacheEntry) SessionDataStore.getInstance().
                    getSessionData(keyValue, SESSION_DATA_CACHE_NAME);
        }
        return cacheEntry;
    }

    @Override
    public void clearCacheEntry(CacheKey key) {
        if (useCache) {
            super.clearCacheEntry(key);
        }
        String keyValue = ((ServiceTicketCacheKey) key).getSessionDataKey();
        SessionDataStore.getInstance().clearSessionData(keyValue, SESSION_DATA_CACHE_NAME);
    }
}