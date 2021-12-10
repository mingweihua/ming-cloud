package cn.sysu.service.impl;

import cn.sysu.mapper.ZKCollectMapper;
import cn.sysu.pojo.ZK_collect;
import cn.sysu.service.ZKDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ZKDataServiceImpl implements ZKDataService {

    @Autowired
    private ZKCollectMapper zkCollectMapper;

    @Override
    public List<ZK_collect> getAllCollectData(){
        return zkCollectMapper.selectList(null);
    }
}
