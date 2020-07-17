package com.jianlang.article.utils;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode root;

    public Trie(){
        root = new TrieNode();
        root.var = ' ';
    }

    public void insert(String word){
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if (!node.children.keySet().contains(c)){
                node.children.put(c, new TrieNode(c));
            }
            node = node.children.get(c);
        }
        node.isWord = true;
    }

    public List<String> startWith(String prefix){
        List<String> match = new ArrayList<>();
        TrieNode node = root;
        for(int i = 0; i < prefix.length(); i++){
            char c = prefix.charAt(i);
            if(!node.children.keySet().contains(c)) return match;
            node = node.children.get(c);
            if(!node.containLongTail){
                for (char cc : node.children.keySet()){
                    match.add(prefix+cc);
                }
            }else{
                //
            }
        }
        return match;
    }
}
