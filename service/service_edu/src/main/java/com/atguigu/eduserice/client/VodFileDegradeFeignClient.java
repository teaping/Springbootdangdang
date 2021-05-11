package com.atguigu.eduserice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {

    //出错之后执行
    @Override
    public R removeAlyVideo(String id) {
        return R.err().message("删除视频出错了");
    }

    @Override
    public R deletebatch(List<String> videoIdList) {
        return R.err().message("删除多个视频出错了");
    }
}
