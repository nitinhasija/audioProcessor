# audioProcessor
# This project only accepts .wav format files and recognises speech and convert it into text.


curl --location 'http://localhost:8080/upload-audio' \
--header 'Content-Type: application/json' \
--form 'file=@"/C:/Users/nitin_hasija/Downloads/sample.wav"'
