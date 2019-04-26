package com.zfy.adapter.annotation;

import com.zfy.adapter.function._Function;
import com.zfy.adapter.model.ModelType;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2018/11/21
 * Describe :
 *
 * @author chendong
 */
public class AnnotationParser {

    // 解析单类型
    public ModelType parserItemType(Object target, int layoutId) {
        XItemType option = target.getClass().getAnnotation(XItemType.class);
        if (option == null) {
            return null;
        }
        return makeModelTypeByItemTypeAnno(option, layoutId);
    }


    private ModelType makeModelTypeByItemTypeAnno(XItemType option, int layoutId) {
        if (option == null) {
            return null;
        }
        ModelType modelType = new ModelType(option.type());
        modelType.spanSize = option.spanSize();
        modelType.enableClick = option.enableClick();
        modelType.layoutId = option.layoutId();
        modelType.enableLongPress = option.enableLongPress();
        modelType.enableDbClick = option.enableDbClick();
        modelType.enableDrag = option.enableDrag();
        modelType.enableSwipe = option.enableSwipe();
        modelType.enablePin = option.enablePin();
        if (modelType.layoutId <= 0 && layoutId > 0) {
            modelType.layoutId = layoutId;
        }
        return modelType;
    }

    // 解析多类型
    public List<ModelType> parserItemTypes(Object target, _Function<Integer, Integer> layoutMapper) {
        XItemTypes option = target.getClass().getAnnotation(XItemTypes.class);
        if (option == null) {
            return null;
        }
        List<ModelType> modelTypes = new ArrayList<>();
        XItemType[] value = option.value();
        for (XItemType itemType : value) {
            Integer layoutId = layoutMapper.map(itemType.type());
            if (layoutId != null) {
                ModelType modelType = makeModelTypeByItemTypeAnno(itemType, layoutId);
                if (modelType != null) {
                    modelTypes.add(modelType);
                }
            }
        }
        return modelTypes;
    }
}
