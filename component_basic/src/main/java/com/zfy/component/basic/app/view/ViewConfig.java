package com.zfy.component.basic.app.view;

/**
 * CreateAt : 2018/10/9
 * Describe :
 *
 * @author chendong
 */
public class ViewConfig {

    private int   layout;
    private int   vmId;
    private Class vmClazz;
    private Class pClazz;

    private ViewConfig() {
    }

    public static ViewConfig makeMvvm(int layout, int vmId, Class vmClazz) {
        ViewConfig viewConfig = new ViewConfig();
        viewConfig.layout = layout;
        viewConfig.vmId = vmId;
        viewConfig.vmClazz = vmClazz;
        return viewConfig;
    }

    public static ViewConfig makeMvp(int layout, Class pClazz) {
        ViewConfig viewConfig = new ViewConfig();
        viewConfig.layout = layout;
        viewConfig.pClazz = pClazz;
        return viewConfig;
    }


    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    public Class getVmClazz() {
        return vmClazz;
    }

    public void setVmClazz(Class vmClazz) {
        this.vmClazz = vmClazz;
    }

    public Class getpClazz() {
        return pClazz;
    }

    public void setpClazz(Class pClazz) {
        this.pClazz = pClazz;
    }
}
