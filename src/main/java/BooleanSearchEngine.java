import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> index = new HashMap<>();


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File[] pdfFiles = pdfsDir.listFiles();
        for (File pdfFile : pdfFiles) {
            var doc = new PdfDocument(new PdfReader(pdfFile));
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = (text.toLowerCase().split("\\P{IsAlphabetic}+"));

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (var word : freqs.keySet()) {
                    List<PageEntry> pageEntries = new ArrayList<>();
                    if (index.containsKey(word)){
                        pageEntries = index.get(word);
                    }
                    pageEntries.add(new PageEntry(pdfFile.getName(), i, freqs.get(word)));
                    Collections.sort(pageEntries, Collections.reverseOrder());
                    index.put(word, pageEntries);

                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return index.get(word.toLowerCase());

    }
}
