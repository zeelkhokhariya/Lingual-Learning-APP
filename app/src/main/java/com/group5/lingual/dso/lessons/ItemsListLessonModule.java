package com.group5.lingual.dso.lessons;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//A lesson module for listing a collection of items consisting of an item name and several heading-content pairs
public class ItemsListLessonModule implements ILessonModule
{
    //Separator to allow multiple strings to be stored in the contentData column of the database
    //(ex. contentData column contains 'stringA~~~stringB~~~stringC')
    private static final String DB_CONTENT_SEPARATOR = "~~~";

    private boolean contentInitialized = false; //Whether the module's content has yet been initialized

    private String heading;             //Heading for the module as a whole
    private List<String> itemHeadings;  //The list of headings displayed in the details of each item
    private List<Item> items;           //List of items, each containing a name and a list of contents corresponding to the headings

    public ItemsListLessonModule() {}

    public ItemsListLessonModule(String heading, List<String> itemHeadings, List<String> itemNames, List<List<String>> itemContents)
    {
        this.heading = heading;
        this.itemHeadings = itemHeadings;

        //Ensure the number of names matches the number of content lists
        if(itemNames.size() != itemContents.size())
            throw new IllegalArgumentException("Number of item names did not match number of content lists");

        items = new ArrayList<Item>();
        for(int i = 0; i < itemNames.size(); i++)
        {
            //Ensure the number of contents in the list matches the number of headings
            if(itemContents.get(i).size() != itemHeadings.size())
                throw new IllegalArgumentException("Size of item content list " + i + " did not match the number of headings");

            items.add(new Item(itemNames.get(i), itemContents.get(i)));
        }

        contentInitialized = true;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        //Ensure the other is a item list lesson module
        if(!(obj instanceof ItemsListLessonModule))
            return false;

        ItemsListLessonModule other = (ItemsListLessonModule)obj;

        //Compare values
        return getContentData().equals(other.getContentData());
    }

    //The module has nothing to complete, so it is always completed
    @Override
    public boolean isCompleted()
    {
        return true;
    }

    @Override
    public String getContentData()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        //Append the module heading to the content data string
        String result = heading + DB_CONTENT_SEPARATOR;

        //Append each item heading to the data string
        for(String s : itemHeadings)
            result += s + DB_CONTENT_SEPARATOR;

        //Append an extra separator to mark the end of the item headings
        result += DB_CONTENT_SEPARATOR;

        //Append each item's name followed by its contents to the data string
        for(Item i : items)
        {
            result += i.getName() + DB_CONTENT_SEPARATOR;
            for(String s : i.getContents())
                result += s + DB_CONTENT_SEPARATOR;
        }

        //Remove final trailing separator
        result = result.substring(0, result.length() - DB_CONTENT_SEPARATOR.length());

        return result;
    }

    @Override
    public void initializeContent(String contentData)
    {
        //Ensure module not already initialized
        if(contentInitialized)
            throw new IllegalStateException("Lesson module already initialized");

        try
        {
            //Tokenize the data string
            String[] tokens = contentData.split(DB_CONTENT_SEPARATOR);

            //Get the module heading from the tokens
            heading = tokens[0];

            //Get the item headings from the tokens until an empty token is found
            itemHeadings = new ArrayList<String>();
            int curr = 1;
            while (tokens[curr].length() > 0)
                itemHeadings.add(tokens[curr++]);

            //Get each item's name and contents from the tokens, based on the number of item headings read
            curr++; //Skip the empty token
            items = new ArrayList<Item>();
            while (curr < tokens.length)
            {
                String name = tokens[curr++];
                List<String> contents = new ArrayList<String>();
                for (int i = 0; i < itemHeadings.size(); i++)
                    contents.add(tokens[curr++]);

                items.add(new Item(name, contents));
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException("Provided content data does not match module format", e);
        }

        contentInitialized = true; //Mark content as initialized
    }

    //Gets the heading of the module
    public String getHeading()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        return heading;
    }

    //Gets the list of item names
    public List<String> getItems()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        List<String> itemNames = new ArrayList<String>();
        for(Item i : items)
            itemNames.add(i.getName());

        return itemNames;
    }

    //Gets the list of headings which each item has contents for
    public List<String> getItemHeadings()
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        return itemHeadings;
    }


    //Gets the list of contents corresponding to the item headings
    public List<String> getItemContents(int itemIndex)
    {
        if(!contentInitialized)
            throw new IllegalStateException("Lesson module content not initialized");

        return items.get(itemIndex).getContents();
    }

    //Contains an item's name and its contents
    private class Item implements Serializable
    {
        private String name;            //The name of the item
        private List<String> contents;  //The contents of the item corresponding to each of the item headings

        public Item(String name, List<String> contents)
        {
            this.name = name;
            this.contents = contents;
        }

        public String getName() { return name; }
        public List<String> getContents() { return contents; }
    }
}
