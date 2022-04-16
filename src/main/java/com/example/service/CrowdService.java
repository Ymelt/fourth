package com.example.service;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Crowd;
import com.example.mapper.CrowdMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrowdService extends ServiceImpl<CrowdMapper, Crowd> {

    public List<Crowd> getList(){
        QueryWrapper<Crowd> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("性别","男");
        List<Crowd> crowdList = this.list(queryWrapper);
        return crowdList;
    }

}
