package com.jyd.test01;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

public class Test01 {
	private static final Logger mLogger = LoggerFactory.getLogger(Test01.class);
	
	private final BloomFilter<String> dealIdBloomFilter = BloomFilter.create(new Funnel<String>() {

        private static final long serialVersionUID = 1L;

        @Override
        public void funnel(String arg0, PrimitiveSink arg1) {

            arg1.putString(arg0, Charsets.UTF_8);
        }

    }, 1024*1024*32);
	
	public synchronized boolean containsDealId(String deal_id){

        if(StringUtils.isEmpty(deal_id)){
            mLogger.warn("deal_id is null");
            return true;
        }

        boolean exists = dealIdBloomFilter.mightContain(deal_id);
        if(!exists){
            dealIdBloomFilter.put(deal_id);
        }
        return exists;
    }
	
	public static void main(String[] args) {
		Test01 t = new Test01();
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		int i =0;
		while (i<100) {
			list.add(i);
			i++;
		}
		int size = list.size();
		for (int j = 0; j < size; j++) {
			boolean exists = t.containsDealId(""+list.get(j));
		}
		
		boolean exists = t.containsDealId("102");
		
		System.out.println(exists);
	}
}
