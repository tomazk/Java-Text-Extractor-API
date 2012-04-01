Java Text Extractor API
=======================

Web API for Java based text extractors. Implemented using Play framework.

Author
======

Tomaž Kovačič <tomaz.kovacic@gmail.com>

Extractors supported
====================

* `Boilerpipe <http://code.google.com/p/boilerpipe/>`_
* `Goose <https://github.com/jiminoc/goose/>`_ (using my `fork <https://github.com/tomazk/goose>`_)

API Documentation
=================

*Note:* All parameters should be encoded using ``x-www-form-urlencoded`` 

Boilerpipe API
--------------

**method:** ``POST``

**endpoint:** ``http://yourdomain/boilerpipe/extract/``

**params:**

* ``extractorType`` : ``(article|default|sentence)``
* ``rawHtml`` : html content

**JSON response format:**

::

	{	
		"result": RESULT_TEXT
		"status": (OK|ERROR)
		"errorMsg": ERROR_MESSAGE (optional)
	}	
	
TTR API
--------------

**method:** ``POST``

**endpoint:** ``http://yourdomain/trr/extract/``

**params:**

* ``extractorType`` : for now only ``default`` is available
* ``rawHtml`` : html content

**JSON response format:**

::

	{	
		"result": RESULT_TEXT
		"status": (OK|ERROR)
		"errorMsg": ERROR_MESSAGE (optional)
	}	

Goose API
---------

**method:** ``POST``

**endpoint:** ``http://yourdomain/goose/extract/``

**params:**

* ``rawHtml`` : html content

**JSON response format:**

::

	{	
		"result": RESULT_TEXT
		"status": (OK|ERROR)
		"errorMsg": ERROR_MESSAGE (optional)
	}	

Dependencies
============

* `Play <http://www.playframework.org/>`_ framework v1.1.1.

Licence
=======

* Everything that's not in the ``/lib/`` or ``TextExtrApi/app/edu/illinois/dais/trr/`` directory is licenced under GPLv3. 
* Jar packages in the ``/lib/`` are licenced under their respective licence listed below:

    * `Boilerpipe <http://code.google.com/p/boilerpipe/>`_ - Apache Licence 2.0
    * `NekoHTML <http://nekohtml.sourceforge.net/>`_ - Apache Licence 2.0
    * `Xerces <http://xerces.apache.org/>`_ - Apache Licence 2.0
    * `Goose <https://github.com/jiminoc/goose>`_ - (no licence provided)
    * Weka - GPLv3
	* htmllexer - Common Public License
	* htmlparser - Common Public License
	* Log4j - Apache Apache Software License v2
* Code in ``TextExtrApi/app/edu/illinois/dais/trr/`` has a Research only licence and was provided by `Tim Weninger <http://www.cs.illinois.edu/homes/weninge1/cetr/>`_  


Copyright (C) Tomaž Kovačič

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
