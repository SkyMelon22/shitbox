package com.example.textanalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utility class responsible for extracting plain text from files in various
 * formats. Supported formats include plain text ({@code .txt}) and Office
 * Open XML Word documents ({@code .docx}). For legacy Word documents
 * ({@code .doc}) a best‑effort extraction is performed by filtering
 * printable characters from the binary data.
 */
public class DocumentParser {

    /**
     * Parses the given file and returns its textual contents. The type of
     * parser used depends on the file extension. Unsupported extensions
     * result in an {@link IOException}.
     *
     * @param filePath path to the file as a {@link String}
     * @return extracted text (possibly empty)
     * @throws IOException if the file cannot be read or is of an unsupported type
     * @throws IllegalArgumentException if {@code filePath} is {@code null}
     */
    public String parse(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path must not be null.");
        }
        Path path = Path.of(filePath);
        String extension = getExtension(path.getFileName().toString()).toLowerCase();
        switch (extension) {
            case "txt":
                return parseTxt(path);
            case "docx":
                return parseDocx(path);
            case "doc":
                return parseDoc(path);
            default:
                throw new IOException("Unsupported file type: " + extension);
        }
    }

    /**
     * Reads a UTF‑8 or Windows‑1251 encoded text file into a string. If UTF‑8
     * decoding fails the method retries using Windows‑1251, which is common
     * for Russian language text files.
     *
     * @param path path to the file
     * @return file contents as a string
     * @throws IOException if an I/O error occurs
     */
    private String parseTxt(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            return Files.readString(path, Charset.forName("windows-1251"));
        }
    }

    /**
     * Extracts text from a DOCX file by reading the main document part
     * {@code word/document.xml} and stripping XML tags. This method does not
     * depend on external libraries such as Apache POI and therefore provides
     * basic extraction without formatting.
     *
     * @param path path to the {@code .docx} file
     * @return extracted plain text
     * @throws IOException if the file is invalid or cannot be read
     */
    private String parseDocx(Path path) throws IOException {
        StringBuilder text = new StringBuilder();
        try (ZipFile zipFile = new ZipFile(path.toFile())) {
            ZipEntry entry = zipFile.getEntry("word/document.xml");
            if (entry == null) {
                throw new IOException("Invalid docx file: missing document.xml");
            }
            try (InputStream stream = zipFile.getInputStream(entry)) {
                byte[] bytes = stream.readAllBytes();
                String xml = new String(bytes, StandardCharsets.UTF_8);
                // remove all XML tags
                xml = xml.replaceAll("<[^>]+>", " ");
                // replace common XML entities with their actual characters
                xml = xml.replaceAll("&amp;", "&");
                xml = xml.replaceAll("&lt;", "<");
                xml = xml.replaceAll("&gt;", ">");
                xml = xml.replaceAll("&quot;", "\"");
                xml = xml.replaceAll("&apos;", "'");
                text.append(xml);
            }
        }
        return text.toString();
    }

    /**
     * Performs a very rudimentary extraction of text from a binary DOC file by
     * reading its bytes and filtering out non‑alphanumeric characters. This
     * method does not understand the Word binary format and should only be
     * considered a fallback when no other library is available. The result
     * may contain artefacts and should not be relied on for precise parsing.
     *
     * @param path path to the {@code .doc} file
     * @return extracted text
     * @throws IOException if reading the file fails
     */
    private String parseDoc(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int unsigned = b & 0xFF;
            char c = (char) unsigned;
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                sb.append(c);
            } else {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * Returns the file extension (without leading dot) for the given file
     * name. If the file has no extension an empty string is returned.
     *
     * @param fileName name of the file
     * @return extension or empty string
     */
    private String getExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot < 0) {
            return "";
        }
        return fileName.substring(lastDot + 1);
    }
}