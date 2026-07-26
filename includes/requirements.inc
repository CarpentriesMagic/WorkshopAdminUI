        <dd class="col py-2 px-3 mx-0 mb-0">
            {% comment %}
            SPECIAL REQUIREMENTS

            Modify the block below if there are any special requirements.
            {% endcomment %}
            <p id="requirements">
            {% if online == "false" %}
                Participants must bring a laptop with a
                Linux
            {% else %}
                Participants must have access to a computer with a
                Mac, Linux, or Windows operating system (not a tablet, Chromebook, etc.) that they have administrative privileges on.
            {% endif %}
            They should have a few specific software packages installed (listed <a href="#setup">below</a>).
            </p>
        </dd>
