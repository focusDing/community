package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
public class SensitiveFilter {

  private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

  //替换符
  private static final String REPLACEMENT = "***";

  private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
      try(
              //字节流转换为字符流
              //字符流转换为缓冲流，更方便
              InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
              BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
             ){
            String keyword;
            while((keyword = reader.readLine()) != null){
                //添加到前缀树
                this.addKeyword(keyword);
            }
      }catch (IOException e){
            logger.error("加载敏感词文件失败：" + e.getMessage());
      }
    }

    //将一个敏感词加到前缀树中
    private void addKeyword(String keyword) {
      TrieNode tempNode = rootNode;
      for(int i=0;i<keyword.length();i++){
          char c = keyword.charAt(i);
          TrieNode subNode = tempNode.getSubNode(c);
          if(subNode == null){
              //初始化子结点
              subNode = new TrieNode();
              tempNode.addSubNode(c,subNode);
          }
          //指向子节点，进行下一轮循环
          tempNode = subNode;

          //设置结束标示
          if (i == keyword.length()-1){
              tempNode.setKeywordEnd(true);
          }
      }
    }


    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }

        //前缀树遍历指针
        TrieNode tempNode = rootNode;

        //文本序列双指针
        int begin,end;
        begin = end = 0;

        StringBuilder sb = new StringBuilder();

        while (end < text.length()){
            char c = text.charAt(end);

            //跳过混淆符号
            if(isSymbol(c)){
                //若前缀树指针处于根结点，说明这个符号
                if(tempNode == rootNode){
                    sb.append(c);
                    ++begin;
                }
                //无论前缀树指针处于哪里，判断一个字符，end指针都必须向前移动
                ++end;
                continue;
            }

            //检查下级结点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //以begin开头的不是敏感词
                sb.append(text.charAt(begin));

                //进入下一个判定位置
                end = ++begin;

                //重新指向根部结点
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                //发现敏感词[begin,end]
                sb.append(REPLACEMENT);//替换字符串
                //进入下一个位置
                begin = ++end;
                //重新指向根部结点
                tempNode = rootNode;
            }else {
                ++end;
            }

        }

        //将最后一批字符统计入结果
        sb.append(text.substring(begin));
        return sb.toString();

    }

    //判断是否为混淆符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    /**
     * 前缀树的结构
     */
    private class TrieNode{

        //关键词结束的标识
        private boolean isKeywordEnd = false;

        //子结点key是下级字符，value是下级结点
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
