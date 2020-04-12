#Android 撸一个微信清理

##首先为什么
+ 微信用久了，各种垃圾文件（想想那些群）
+ 微信自带清理功能？（我->设置->通用->微信存储空间->管理微信存储空间->管理当前微信账号聊天数据），六步之后就可以选择聊天对象删除所有文件（当然也可以再加一步点进去感受一下）
+ 当然这个属于不常用功能，不好用也可以理解（狗头保命）
+ 自己撸一个微信文件管理（我不管我就要一键看到文件分类，不想要的就删掉）

可以在这里先预览一波最终的成果:
[代码地址](https://github.com/DavidInChina/DavidInChina-Cleaner)

![列表页面](https://github.com/DavidInChina/DavidInChina-Cleaner/blob/master/device-2020-04-12-225407.png ''列表'')
![列表页面](https://github.com/DavidInChina/DavidInChina-Cleaner/blob/master/device-2020-04-12-225446.png ''列表'')
![列表页面](https://github.com/DavidInChina/DavidInChina-Cleaner/blob/master/device-2020-04-12-225516.png ''列表'')

##然后怎么做
+ 找到要管理的文件们
 
   这一点其实就是看微信把用户的聊天文件存在什么地方了，这里我们首先要想文件们包括哪些，图片？视频？还有文件表情包，这些东西微信可没有直接摆在包名目录下面等我们去把玩（其实/Tencent/MicroMsg这个路径下确实可以看到很多文件）。但是看到文件不代表我们可以管理，或者自己随意的删除，万一删错了损失的可就是自己了。
   
   在这里似乎路子有点走不通，但是我们可以思路开阔点想想，比如找一款类似功能的软件来分析一下，这样不就可以大致知道该操作哪些东西了么。接下来就好办了，商店关键字搜索一波，找到一个下载量还可以的某微信清理软件，一番体验操作之后拿到了DES加密后的可管理的微信缓存目录顺手分析一波解密之（逆向分析加密破解此处按下不表），现在我们就有了一个可选删除的微信存储文件的路径清单（详情见代码）；
 
+ 开始一个专属微信的文件管理器
   
   >分类
   >>两个大类： 
   >>>可直接删除的缓存文件还有可选删除的其它文件； 
   
   >> 其它文件包括:
   >>>语音、视频、图片、表情包还有文件（压缩包还有文本文件等）
   
   再往下就是展示技术的时候了，埋伏他一手，先整理出各个分类下的文件夹路径存储起来：
   
   ```
    File root = FileUtils.getWechatFilesRoot();//获取微信缓存根目录
        File[] listFiles = root.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            //回调没有数据
            return;
        }
        //添加各个分类对应的文件夹路径
        hashMap.put(CATEGORY_IMAGE, new ArrayList());
        hashMap.put(CATEGORY_EMOJI, new ArrayList());
        hashMap.put(CATEGORY_FILES, new ArrayList());
        hashMap.put(CATEGORY_VIDEO, new ArrayList());
        hashMap.put(CATEGORY_VOICE, new ArrayList());
        hashMap.put(CATEGORY_JUNK, new ArrayList());
        hashMap.put(CATEGORY_CACHE, new ArrayList());
        for (File file : listFiles) {
            if (file.isDirectory()) {
                File[] listFiles2 = file.listFiles();
                if (listFiles2 != null) {
                    for (File file2 : listFiles2) {
                        String name = file2.getName();
                        if (name.equals(CATEGORY_IMAGE) ||
                                name.equals(CATEGORY_VOICE) ||
                                name.equals(CATEGORY_EMOJI) ||
                                name.equals(CATEGORY_VIDEO)
                        ) {
                            ((ArrayList) hashMap.get(name)).add(file2);
                        }
                    }
                }
            }
        }
        ((ArrayList) hashMap.get(CATEGORY_FILES)).add(new File(root, CATEGORY_FILES));
        DBUtils.readDBfils(context);//这里就逆向得到的缓存路径集合
        ((ArrayList) hashMap.get(CATEGORY_JUNK)).addAll(DBUtils.junks);
        ((ArrayList) hashMap.get(CATEGORY_CACHE)).addAll(DBUtils.caches);        
```

这里微信缓存的根目录是：
    
```
        public static File getWechatFilesRoot() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg");
        return !file.exists() ? new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tencent/MicroMsg") : file;
    }
    
```    
跟上一手文件扫描，拿到所有待管理的文件，这里我们要注意切换工作线程，当然，上面的目录也最好是子线程获取。
        
```
        public final void getFilelistAsync(final List<File> list, final getFilesListener listener) {
        if (!this.isDataClear && null != list && list.size() > 0) {
            this.task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    if (!"".equals(getFileTile())) {
                        FileUtils.scannerInnerFiles(new File(FileUtils.getWechatFilesRoot()
                                , "WeiXin"), new FileUtils.getInnerFileListener() {//深度优先遍历出对应文件列表
                            public final void innerFiles(File file, long j) {
                                if (file.getAbsolutePath().endsWith(getFileTile())) {
                                    innerFiles.add(new WechatFile(file.getAbsolutePath(), j, new Date(file.lastModified()).getTime()
                                            , FileUtils.getFileExt(file.getAbsolutePath())));
                                    listener.getFileOneSize(j);
                                }
                            }
                        }, 1, 1);
                    }
                    long size = FileUtils.scannerOutFiles(list, outFiles, getFileTile(), listener);
                    return null;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    if (isDataClear) {
                        if (!outFiles.isEmpty()) {
                            FileUtils.sortByTime(outFiles);//排序
                        }
                        if (!innerFiles.isEmpty()) {
                            FileUtils.sortByTime(innerFiles);//排序
                        }
                        isDataClear = false;
                        if (listener != null) {
                            listener.getFilesFinish(false);//返回结果
                        }
                    }
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    isDataClear = true;//清空数据
                    outFiles.clear();
                    innerFiles.clear();
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (!outFiles.isEmpty()) {
                        FileUtils.sortByTime(outFiles);
                    }
                    if (!innerFiles.isEmpty()) {
                        FileUtils.sortByTime(innerFiles);
                    }
                    isDataClear = false;
                    if (listener != null) {
                        listener.getFilesFinish(true);//返回结果
                    }
                }
            };
            this.task.executeOnExecutor(ThreadPool.getInstance().threadPoolExecutor, new Void[0]);
        } else {
            if (listener != null) {
                listener.getFilesFinish(false);//返回结果
            }
        }
    }
```
到这里我们就拿到了需要管理的微信缓存文件，下面的步骤就是常规图片文件选择器之类的列表文件展示了（具体可以看代码，这边不再赘述）。最后讲下删除的时候要注意本地文件存储和内存列表的同步。
   
 

##还能怎么做
+ 当然还有短视频缓存清理啊（狗头），市面上常见视频应用的缓存路径已经在代码中提供。
+ 图片或者更进一步的文件选择器或者微信文件导出。
+ 优化可以考虑文件的数据库路径缓存（需要考虑合适的增量更新策略），这样可以避免在本地文件数据很多的时候每次都进行全量扫描。
