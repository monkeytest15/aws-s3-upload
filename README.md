S3 InputStream Upload
=====================

Simple implementation of uploading via InputStream.

###Useage
java -jar s3uploader.jar pathtofile

Assuming each upload thread maxes out around 10MB/s, each inputstream is buffered to memory: On a system with 50GB free memory and a 500MB/s network, to saturate execute 50 concurrent uploads of 1GB files each.


###Lessons Learned
1. 'moderate' network speed caps out at around 50MB/s
2. InputStream buffers to memory, must have memory available equal to upload file size (upload will still complete if file exceeds memory, but almost all memory will be consumed)
3. errors happen often, 500 internal errors most common, read timeouts
