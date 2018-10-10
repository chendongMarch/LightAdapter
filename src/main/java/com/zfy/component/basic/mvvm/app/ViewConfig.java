package com.zfy.component.basic.mvvm.app;

/**
 * CreateAt : 2018/10/9
 * Describe :
 *
 * @author chendong
 */
public class ViewConfig {

    private int layout;
    private int vmId;
    private Class vmClazz;

    private ViewConfig() {
    }

    public static ViewConfig make(int layout, int vmId, Class vmClazz) {
        ViewConfig viewConfig = new ViewConfig();
        viewConfig.layout = layout;
        viewConfig.vmId = vmId;
        viewConfig.vmClazz = vmClazz;
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
}
