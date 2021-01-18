package com.fosuchao.compress;

import com.fosuchao.extension.SPI;

/**
 * Created by Chao Ye on 2021/1/18
 */
@SPI
public interface Compress {

    /**
     * 压缩
     */
    byte[] compress(byte[] bytes);

    /**
     * 解压缩
     */
    byte[] decompress(byte[] bytes);
}
