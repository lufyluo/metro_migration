package com.metro.nccc.migration.utils.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metro.nccc.migration.model.vo.usercenter.WechatUserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 下午2:57
 */
@Data
@NoArgsConstructor
public class WechatResponse implements Serializable {
    private Integer errcode;
    private String errmsg;
    private List<WechatUserInfo> userlist;
    @JsonIgnore
    protected ResponseStatus status;

    public Boolean isSuccessed(){
        return errcode==0&& ObjectUtils.nullSafeEquals(errmsg,"ok");
    }
    public Boolean notAuthed(){
        return errcode==60011;
    }

    public Boolean departmentRemoved(){
        return errcode==60003;
    }
}
