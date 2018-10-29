package com.zfy.adapter.inject;

/**
 * CreateAt : 2018/4/22
 * Describe :
 *
 * @author chendong
 */
public class AdapterConfig {

    public static final int[] ARRAY = new int[]{};

    public static AdapterConfig newConfig() {
        return new AdapterConfig();
    }

    private AdapterConfig() {

    }

    private int itemLayoutId;
    private int[] itemTypes = ARRAY;
    private int[] itemLayoutIds;

    private int[] fullSpanTypes = ARRAY;

    private int footerLayoutId;
    private int headerLayoutId;

    private int preloadBottomNum;
    private int preloadTopNum;

    private boolean dbClick;
    private int[] disableClickTypes = ARRAY;

    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public int[] getItemTypes() {
        return itemTypes;
    }

    public int[] getItemLayoutIds() {
        return itemLayoutIds;
    }

    public int[] getFullSpanTypes() {
        return fullSpanTypes;
    }

    public int getFooterLayoutId() {
        return footerLayoutId;
    }

    public int getHeaderLayoutId() {
        return headerLayoutId;
    }

    public int getPreloadTopNum() {
        return preloadTopNum;
    }

    public int getPreloadBottomNum() {
        return preloadBottomNum;
    }

    public boolean isDbClick() {
        return dbClick;
    }


    public boolean isDisableType(int type) {
        if (disableClickTypes == null) {
            return false;
        }
        for (int disableClickType : disableClickTypes) {
            if (disableClickType == type) {
                return true;
            }
        }
        return false;
    }

    //////////////////////////////  -- setter --  //////////////////////////////

    public AdapterConfig dbClick(boolean dbClick) {
        this.dbClick = dbClick;
        return this;
    }

    public AdapterConfig disableClickTypes(int... disableClickTypes) {
        this.disableClickTypes = disableClickTypes;
        return this;
    }

    public AdapterConfig preloadBottom(int preloadBottomNum) {
        this.preloadBottomNum = preloadBottomNum;
        return this;
    }

    public AdapterConfig preloadTop(int preloadTopNum) {
        this.preloadTopNum = preloadTopNum;
        return this;
    }

    public AdapterConfig headerLayoutId(int headerLayoutId) {
        this.headerLayoutId = headerLayoutId;
        return this;
    }

    public AdapterConfig footerLayoutId(int footerLayoutId) {
        this.footerLayoutId = footerLayoutId;
        return this;
    }

    public AdapterConfig fullSpanTypes(int... fullSpanTypes) {
        this.fullSpanTypes = fullSpanTypes;
        return this;
    }

    public AdapterConfig itemLayoutIds(int... itemLayoutIds) {
        this.itemLayoutIds = itemLayoutIds;
        return this;
    }

    public AdapterConfig itemTypes(int... itemTypes) {
        this.itemTypes = itemTypes;
        return this;
    }

    public AdapterConfig itemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
        return this;
    }
}
