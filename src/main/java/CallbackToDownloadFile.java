import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.*;

class CallbackToDownloadFile implements Callback {
    private File dir;
    private File fileToBeDownloaded;

    public CallbackToDownloadFile(String directory, String fileName) {
        this.dir = new File(directory);
        this.fileToBeDownloaded = new File(this.dir.getAbsolutePath() + File.separator + fileName);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (fileToBeDownloaded.exists()) {
            fileToBeDownloaded.delete();
        }

        try {
            fileToBeDownloaded.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        InputStream inputStream = response.body().byteStream();
        OutputStream outputStream = new FileOutputStream(this.fileToBeDownloaded);
        final int BUFFER_SIZE = 2048;
        byte[] data = new byte[BUFFER_SIZE];

        int count = 0;
        long total = 0;

        while ((count = inputStream.read(data)) != -1) {
            total += count;
            outputStream.write(data, 0, count);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
        System.out.println("Downloading complete.");
    }
}