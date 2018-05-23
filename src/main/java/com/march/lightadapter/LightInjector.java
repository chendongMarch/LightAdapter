package com.march.lightadapter;

import android.support.v7.widget.RecyclerView;

import com.march.lightadapter.helper.LightLogger;
import com.march.lightadapter.inject.AdapterConfig;
import com.march.lightadapter.inject.AdapterLayout;
import com.march.lightadapter.inject.Click;
import com.march.lightadapter.inject.Footer;
import com.march.lightadapter.inject.FullSpan;
import com.march.lightadapter.inject.Header;
import com.march.lightadapter.inject.PreLoading;
import com.march.lightadapter.module.FullSpanModule;
import com.march.lightadapter.module.HFModule;
import com.march.lightadapter.module.LoadMoreModule;
import com.march.lightadapter.module.TopLoadMoreModule;
import com.march.lightadapter.module.UpdateModule;

import java.lang.reflect.Field;

/**
 * CreateAt : 2018/2/24
 * Describe : 向 adapter 中注入配置
 *
 * @author chendong
 */
public class LightInjector {

    private static final String TAG = LightInjector.class.getSimpleName();

    private static Field getAdapterField(Object target, Object current) {
        Field[] fields = target.getClass().getDeclaredFields();
        // find field
        Field adapterField = null;
        for (Field field : fields) {
            try {
                if (field.getType().equals(LightAdapter.class)) {
                    field.setAccessible(true);
                    Object obj = field.get(target);
                    if (obj != null && obj.hashCode() == current.hashCode()) {
                        adapterField = field;
                    }
                    break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (adapterField == null) {
            throw new IllegalArgumentException("at least need item layout id");
        }
        return adapterField;
    }

    private static AdapterConfig parseAnnotation(Object host, LightAdapter adapter) {
        Field field = getAdapterField(host, adapter);

        AdapterConfig config = AdapterConfig.newConfig();
        PreLoading preLoadingAnno = field.getAnnotation(PreLoading.class);
        if (preLoadingAnno != null) {
            config.preloadTop(preLoadingAnno.bottom());
            config.preloadBottom(preLoadingAnno.top());
        }
        Header headerAnno = field.getAnnotation(Header.class);
        if (headerAnno != null) {
            config.headerLayoutId(headerAnno.value());
        }
        Footer footerAnno = field.getAnnotation(Footer.class);
        if (footerAnno != null) {
            config.footerLayoutId(footerAnno.value());
        }
        FullSpan fullSpanAnno = field.getAnnotation(FullSpan.class);
        if (fullSpanAnno != null) {
            config.fullSpanTypes(fullSpanAnno.value());
        }
        Click clickAnno = field.getAnnotation(Click.class);
        if (clickAnno != null) {
            config.dbClick(clickAnno.dbClick());
            config.disableClickTypes(clickAnno.disableTypes());
        }
        AdapterLayout adapterLyAnno = field.getAnnotation(AdapterLayout.class);
        if (adapterLyAnno != null) {
            if (adapterLyAnno.value() > 0) {
                config.itemLayoutId(adapterLyAnno.value());
            } else {
                config.itemLayoutId(adapterLyAnno.itemLayoutId());
            }
            config.itemTypes(adapterLyAnno.itemTypes());
            config.itemLayoutIds(adapterLyAnno.itemLayoutIds());
        }
        return config;
    }

    public static void initAdapter(LightAdapter adapter, Object targetHost,
            RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        AdapterConfig config = parseAnnotation(targetHost, adapter);
        initAdapter(adapter, config, recyclerView, layoutManager);
    }

    public static void initAdapter(LightAdapter adapter, AdapterConfig config,
            RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        adapter.setAdapterConfig(config);
        if (config.getPreloadBottomNum() >= 0) {
            adapter.addModule(new LoadMoreModule(config.getPreloadBottomNum()));
        }
        if (config.getPreloadTopNum() >= 0) {
            adapter.addModule(new TopLoadMoreModule(config.getPreloadTopNum()));
        }
        if (config.getHeaderLayoutId() > 0 || config.getFooterLayoutId() > 0) {
            adapter.addModule(new HFModule(adapter.getContext(), config.getHeaderLayoutId(), config.getFooterLayoutId()));
            adapter.addModule(new FullSpanModule());
        }
        if (config.getFullSpanTypes() != null && config.getFullSpanTypes().length > 0) {
            FullSpanModule fullSpanModule = new FullSpanModule();
            int[] fullSpanTypes = config.getFullSpanTypes();
            fullSpanModule.addFullSpanType(fullSpanTypes);
            adapter.addModule(fullSpanModule);
        }
        int itemLayoutId = config.getItemLayoutId();
        int[] itemTypes = config.getItemTypes();
        int[] itemLayoutIds = config.getItemLayoutIds();
        if (itemLayoutId > 0) {
            adapter.addType(LightAdapter.TYPE_DEFAULT, itemLayoutId);
        } else if (itemTypes.length != 0 && itemTypes.length == itemLayoutIds.length) {
            for (int i = 0; i < itemTypes.length; i++) {
                adapter.addType(itemTypes[i], itemLayoutIds[i]);
            }
        } else {
            LightLogger.e(TAG, "itemTypes.length = " + itemTypes.length);
        }
        initAdapter(adapter, recyclerView, layoutManager);
    }

    private static void initAdapter(LightAdapter adapter, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        adapter.addModule(new UpdateModule());
        if (recyclerView != null) {
            if (layoutManager != null) {
                recyclerView.setLayoutManager(layoutManager);
            }
            recyclerView.setAdapter(adapter);
        }
    }
}
