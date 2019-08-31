package com.zfy.adapter.x;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
public class LxContext {

    private Object data;
    private int    pos;
    private LxVh   holder;


    public <D> D getData() {
        return (D) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public LxVh getHolder() {
        return holder;
    }

    public void setHolder(LxVh holder) {
        this.holder = holder;
    }
}
