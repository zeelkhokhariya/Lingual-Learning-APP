package com.group5.lingual.dso;

import java.util.Collection;

//An object that can be tagged with strings for categorization and filtration purposes
public interface ITaggable
{
    //Add, remove, and check for tags assigned to this object
    public void addTag(String tag);
    public void removeTag(String tag);
    public boolean hasTag(String tag);
    public boolean hasTags(Collection<String> tags);

    //Get the full collection of tags on this object
    public Collection<String> getAllTags();
}
