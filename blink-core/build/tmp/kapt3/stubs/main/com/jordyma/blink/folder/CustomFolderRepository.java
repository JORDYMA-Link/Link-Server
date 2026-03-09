package com.jordyma.blink.folder;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Lcom/jordyma/blink/folder/CustomFolderRepository;", "", "deleteFolder", "", "folder", "Lcom/jordyma/blink/folder/Folder;", "findAllByUser", "", "user", "Lcom/jordyma/blink/user/User;", "blink-core"})
public abstract interface CustomFolderRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.jordyma.blink.folder.Folder> findAllByUser(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.user.User user);
    
    public abstract void deleteFolder(@org.jetbrains.annotations.NotNull()
    com.jordyma.blink.folder.Folder folder);
}