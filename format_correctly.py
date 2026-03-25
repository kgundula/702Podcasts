with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'r') as f:
    content = f.read()

import re
# let's just rewrite the whole file manually from line 140 to the end to be absolutely sure.
# wait we can just do a replace

fixed_section = """                        for (int y = 0; y < attrs.getLength(); y++) {
                            Node attr = attrs.item(y);
                            if (attr.getNodeName().equalsIgnoreCase("url")) {
                                podcast_url = attr.getNodeValue();
                            } else if (attr.getNodeName().equalsIgnoreCase("type")) {
                                podcast_type = attr.getNodeValue();
                            }
                        }
                        Podcast podcast = new Podcast(title, description, pubDate, podcast_type, podcast_url);
                        podcastList.add(podcast);
                    }
                    if (mPodcastAdapter == null) {
                        mPodcastAdapter = new PodcastAdapter(podcastList);
                        mPodcastRecyclerView.setAdapter(mPodcastAdapter);
                    } else {
                        mPodcastAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
"""

# Replace everything from `for (int y = 0; y < attrs.getLength(); y++) {` onwards
content = re.sub(r'                        for \(int y = 0; y < attrs\.getLength\(\); y\+\+\) \{.*', fixed_section, content, flags=re.DOTALL)

with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'w') as f:
    f.write(content)
