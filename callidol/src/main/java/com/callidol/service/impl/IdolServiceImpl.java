package com.callidol.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.callidol.common.CIResult;
import com.callidol.common.IdolResult;
import com.callidol.mapper.IdolMapper;
import com.callidol.pojo.Idol;
import com.callidol.service.IdolService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class IdolServiceImpl implements IdolService{
	
	@Autowired
	private IdolMapper idolMapper;

	@Override
	public CIResult searchIdolByName(String name) {
	//	Idol idol = new Idol();
//		idol.setName(name);
		// List<Idol> idolList = idolMapper.select(idol);
		Example example = new Example(Idol.class);
		Criteria criteria = example.createCriteria();
		criteria.andLike("name", "%" + name+ "%");
		List<Idol> idolList = idolMapper.selectByExample(example);
		if(idolList.size() == 0) {
			return CIResult.error("没有查询到匹配的明星");
		}
		
		List<IdolResult> idolResults= new ArrayList<>();
		
		for(Idol idol: idolList) {
			IdolResult idolResult = new IdolResult();
			idolResult.setId(idol.getId());
			idolResult.setName(idol.getName());
			idolResult.setPic(idol.getPic());
//			idolResult.setBrief(idol.getBrief()); 
			
			//TODO 当前默认100
			idolResult.setCalled(100);
			
			idolResults.add(idolResult);
		}
		
		
		return CIResult.ok("ok", idolResults);
	}

	@Override
	public CIResult getIdolInfoById(Long id) {
		Idol idolQ = new Idol();
		idolQ.setId(id);
		Idol idol = idolMapper.selectOne(idolQ);
//		Idol idol = idolMapper.selectByPrimaryKey(id); 
		
		IdolResult idolResult = new IdolResult();
		idolResult.setId(idol.getId());
		idolResult.setName(idol.getName());
		idolResult.setPic(idol.getPic());
		idolResult.setBrief(idol.getBrief());
		
		//TODO 当前默认100
		idolResult.setCalled(100);
		idolResult.setRank(100);//TODO 当前默认100
		
		return CIResult.ok("找到明星信息", idolResult);
	}

}
