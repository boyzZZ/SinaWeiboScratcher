package com.fly.parse.type;

import org.jsoup.nodes.Element;

/**
 * The Interface for parse Weibos
 */
public interface WeiboParseProcessor {
  public void parse(Element e) throws Exception;
}
