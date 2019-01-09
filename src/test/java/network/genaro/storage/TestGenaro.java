package network.genaro.storage;

import org.spongycastle.util.encoders.Hex;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import network.genaro.storage.GenaroCallback.*;

@Test()
public final class TestGenaro {
     private static final String V3JSON = "{\"version\":3,\"id\":\"b3d00298-275f-4f09-96d0-2da6000f2a04\",\"address\":\"aaad65391d2d2eafda9b27326d1e80002a6a3dc8\",\"crypto\":{\"ciphertext\":\"c362de15e57e1fd0ca66b6c2483292ed260000000065164e875eebece257702e\",\"cipherparams\":{\"iv\":\"934b7985f4c60000000f97f89a101ee7\"},\"cipher\":\"aes-128-ctr\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"salt\":\"f5e2b5075600000003c66191656e03cfb19b5e537dcb117ad4fbc1fda46f61c5\",\"n\":262144,\"r\":8,\"p\":1},\"mac\":\"0b8c0000000b9e9de24357bbe74b68baf576ac31e9bfebe3f3d48c5474703df9\"},\"name\":\"Wallet 0\"}";

//    private static String testBridgeUrl = "http://118.31.61.119:8080";
//    private static String testBridgeUrl = "http://127.0.0.1:8080";
    private static String testBridgeUrl = "http://120.77.247.10:8080";
//    private static String testBridgeUrl = "http://47.100.33.60:8080";
//    private static final String testbucketId = "5c0e433cdaa4e03fe1b5b287";
//    private static final String testbucketId = "b5e9bd5fd6f571beee9b035f";
    private static final String testbucketId = "5ba341402e49103d8787e52d";
//    private static final String testbucketId = "5c1b3c70a100262b970883a0";

    public void testGetInfo() {
        Genaro api = new Genaro(testBridgeUrl);
        String info = api.getInfo();
        System.out.println(info);
    }

    public void testGetBuckets() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        CompletableFuture<Void> fu = api.getBuckets(new GetBucketsCallback() {
            @Override
            public void onFinish(Bucket[] buckets) {
                if(buckets.length == 0) {
                    System.out.println("No buckets.");
                } else {
                    for (Bucket b : buckets) {
                        System.out.println(b);
                    }
                }
            }
            @Override
            public void onFail(String error) {
                System.out.println("List buckets failed, reason: " + error + ".");
            }
        });

        fu.join();
    }

    public void testDeleteBucket() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        CompletableFuture<Void> fu = api.deleteBucket("5bfcf77cea9b6322c5abd929", new DeleteBucketCallback() {
            @Override
            public void onFinish() {
                System.out.println("Delete bucket success.");
            }

            @Override
            public void onFail(String error) {
                System.out.println("Delete bucket failed, reason: " + error + ".");
            }
        });

        fu.join();
    }

    public void testRenameBucket() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        CompletableFuture<Void> fu = api.renameBucket(testbucketId, "呵呵", new RenameBucketCallback() {
            @Override
            public void onFinish() {
                System.out.println("Rename bucket success.");
            }
            @Override
            public void onFail(String error) {
                System.out.println("Rename bucket failed, reason: " + error + ".");
            }
        });

        fu.join();
    }

    public void testGetBucket() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        Bucket b = api.getBucket(null, testbucketId);

        if(b == null) {
            System.out.println("Get Bucket failed.");
        } else {
            System.out.println(b);
        }
    }

    public void testListFiles() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        CompletableFuture<Void> fu = api.listFiles(testbucketId, new ListFilesCallback() {
            @Override
            public void onFinish(GenaroFile[] files) {
                if(files.length == 0) {
                    System.out.println("No files.");
                } else {
                    for (GenaroFile b : files) {
                        System.out.println(b.toBriefString());
                    }
                }
            }
            @Override
            public void onFail(String error) {
                System.out.println("List files failed, reason: " + error + ".");
            }
        });

        fu.join();
    }

    public void testListMirrors() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        CompletableFuture<Void> fu = api.listMirrors(testbucketId, "5c1b3d59926e422b70d1a4ea", new ListMirrorsCallback() {
            @Override
            public void onFinish(String text) {
                System.out.println(text);
            }
            @Override
            public void onFail(String error) {
                System.out.println("List mirrors failed, reason: " + error + ".");
            }
        });

        fu.join();
    }

    public void testIsFileExist() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        String fileName = "spam.txt";
        String encryptedFileName = CryptoUtil.encryptMetaHmacSha512(BasicUtil.string2Bytes(fileName), api.getPrivateKey(), Hex.decode(testbucketId));

        boolean exist = api.isFileExist(null, testbucketId, encryptedFileName);
        if(exist) {
            System.out.println("File exists.");
        } else {
            System.out.println("File not exists.");
        }
    }

    public void testDeleteFile() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        CompletableFuture<Void> fu = api.deleteFile(testbucketId, "5c2c7f01bbdd6f2d157dec35", new DeleteFileCallback() {
            @Override
            public void onFinish() {
                System.out.println("Delete file success.");
            }
            @Override
            public void onFail(String error) {
                System.out.println("Delete file failed, reason: " + error + ".");
            }
        });

        fu.join();
    }

    public void testGetFileInfo() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");
        GenaroFile file;

        try {
            file = api.getFileInfo(null, testbucketId, "5c0e6872a72fc61208285155");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(file);
    }

    public void testGetPointers() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        List<Pointer> psa;

        try {
            psa = api.requestPointers(null, testbucketId, "f40da862c00494bb0430e012");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        if(psa.size() == 0) {
            System.out.println("No pointers.");
        } else {
            for (Pointer p : psa) {
                System.out.println(p);
            }
        }
    }

    public void testRequestFrameId() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        Frame frame;
        try {
            frame = api.requestNewFrame(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(frame);
    }

    public void testResolveFile() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

    //        Downloader downloader = api.resolveFile(testbucketId, "5c1b3d59926e422b70d1a4ea", "/Users/dingyi/Genaro/test/download/1.data", true, new ResolveFileCallback() {
    //        Downloader downloader = api.resolveFile(testbucketId, "5c1cb213926e422b70d1aacf", "/Users/dingyi/Genaro/test/download/spam.txt", true, new ResolveFileCallback() {
        Downloader downloader = api.resolveFile(testbucketId, "5c249a44bbdd6f2d157de9c4", "/Users/dingyi/Genaro/test/download/spam.txt", true, new ResolveFileCallback() {
            @Override
            public void onBegin() {
                System.out.println("Download started");
            }
            @Override
            public void onProgress(float progress) {
                System.out.printf("Download progress: %.1f%%\n", progress * 100);
            }
            @Override
            public void onFail(String error) {
                System.out.println("Download failed, reason: " + (error != null ? error : "Unknown"));
            }
            @Override
            public void onCancel() {
                System.out.println("Download is cancelled");
            }
            @Override
            public void onFinish() {
                System.out.println("Download finished");
            }
        });

        downloader.join();
    }

    public void testResolveFileCancel() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

    //            Downloader downloader = api.resolveFile(testbucketId, "5c0a3006bbdd6f2d157dcedb", "/Users/dingyi/Genaro/test/download/cpor-genaro", new ResolveFileCallback() {
        Downloader downloader = api.resolveFile(testbucketId, "5c08d01c963d402a1f3ede80", "/Users/dingyi/Genaro/test/download/r.zip", true, new ResolveFileCallback() {
            @Override
            public void onBegin() {
                System.out.println("Download started");
            }
            @Override
            public void onProgress(float progress) {
                System.out.printf("Download progress: %.1f%%\n", progress * 100);
            }
            @Override
            public void onFail(String error) {
                System.out.println("Download failed, reason: " + (error != null ? error : "Unknown"));
            }
            @Override
            public void onCancel() {
                System.out.println("Download is cancelled");
            }
            @Override
            public void onFinish() {
                System.out.println("Download finished");
            }
        });

        Thread.sleep(3000);
        downloader.cancel();
        downloader.join();
    }

    public void testResolveFileParallel() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        List<Downloader> downloaders = new ArrayList<>();

        try {
            for(int i = 0; i < 5; i++) {
                Downloader downloader = api.resolveFile(testbucketId, "5c249a44bbdd6f2d157de9c4", "/Users/dingyi/Genaro/test/download/spam" + i + ".txt", true, new ResolveFileCallback() {
                    @Override
                    public void onBegin() {
                        System.out.println("Download started");
                    }
                    @Override
                    public void onProgress(float progress) {
                        System.out.printf("Download progress: %.1f%%\n", progress * 100);
                    }
                    @Override
                    public void onFail(String error) {
                        System.out.println("Download failed, reason: " + (error != null ? error : "Unknown"));
                    }
                    @Override
                    public void onCancel() {
                        System.out.println("Download is cancelled");
                    }
                    @Override
                    public void onFinish() {
                        System.out.println("Download finished");
                    }
                });

                downloaders.add(downloader);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }

        downloaders.stream().forEach(downloader -> downloader.join());
    }

    public void testStoreFile() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        Uploader uploader = api.storeFile(true, "/Users/dingyi/test/spam.txt", "spam2001.txt", testbucketId, new StoreFileCallback() {
    //        Uploader uploader = api.storeFile(false, "/Users/dingyi/Downloads/下载器苹果电脑Mac版.zip", "25.zip", testbucketId, new StoreFileCallback() {
    //        Uploader uploader = api.storeFile(false, "/Users/dingyi/Downloads/genaro.tar", "1.tar", testbucketId, new StoreFileCallback() {
            @Override
            public void onBegin(long fileSize) {
                System.out.println("Upload started");
            }

            @Override
            public void onProgress(float progress) {
                System.out.printf("Upload progress: %.1f%%\n", progress * 100);
            }

            @Override
            public void onFail(String error) {
                System.out.println("Upload failed, reason: " + (error != null ? error : "Unknown"));
            }

            @Override
            public void onCancel() {
                System.out.println("Upload is cancelled");
            }

            @Override
            public void onFinish(String fileId) {
                System.out.println("Upload finished, fileId: " + fileId);
            }
        });

        uploader.join();
    }

    public void testStoreFileCancel() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        Uploader uploader = api.storeFile(true, "/Users/dingyi/Downloads/下载器苹果电脑Mac版.zip", "26.zip", testbucketId, new StoreFileCallback() {
            @Override
            public void onBegin(long fileSize) {
                System.out.println("Upload started");
            }
            @Override
            public void onProgress(float progress) {
                System.out.printf("Upload progress: %.1f%%\n", progress * 100);
            }
            @Override
            public void onFail(String error) {
                System.out.println("Upload failed, reason: " + (error != null ? error : "Unknown"));
            }
            @Override
            public void onCancel() {
                System.out.println("Upload is cancelled");
            }
            @Override
            public void onFinish(String fileId) {
                System.out.println("Upload finished, fileId: " + fileId);
            }
        });

        Thread.sleep(5000);
        uploader.cancel();
        uploader.join();
    }

    public void testStoreFileParallel() throws Exception {
        Genaro api = new Genaro(testBridgeUrl, V3JSON, "111111");

        List<Uploader> uploaders = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Uploader uploader = api.storeFile(true, "/Users/dingyi/test/spam.txt", "spam2" + i + ".txt", testbucketId, new StoreFileCallback() {
                @Override
                public void onBegin(long fileSize) {
                    System.out.println("Upload started");
                }

                @Override
                public void onProgress(float progress) {
    //                        System.out.printf("Upload progress: %.1f%%\n", progress * 100);
                }

                @Override
                public void onFail(String error) {
                    System.out.println("Upload failed, reason: " + (error != null ? error : "Unknown"));
                }

                @Override
                public void onCancel() {
                    System.out.println("Upload is cancelled");
                }

                @Override
                public void onFinish(String fileId) {
                    System.out.println("Upload finished, fileId: " + fileId);
                }
            });

            uploaders.add(uploader);
        }

        uploaders.stream().forEach(uploader -> uploader.join());
    }
}