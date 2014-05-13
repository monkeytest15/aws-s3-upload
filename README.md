S3 InputStream Upload
=====================

###Lessons Learned:
1. 'moderate' network speed caps out at around 50MB/s
2. InputStream buffers to memory, must have memory available equal to upload file size (upload will still complete if file exceeds memory, but almost all memory will be consumed)
3. errors happen often, 500 internal errors most common