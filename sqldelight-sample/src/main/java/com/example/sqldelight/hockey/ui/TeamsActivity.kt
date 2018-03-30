package com.example.sqldelight.hockey.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.sqldelight.hockey.R
import com.example.sqldelight.hockey.data.HockeyOpenHelper
import com.example.sqldelight.hockey.data.Team
import com.example.sqldelight.hockey.ui.TeamsActivity.Adapter.ViewHolder

class TeamsActivity : Activity() {
  private val teams by lazy { findViewById<RecyclerView>(R.id.list) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.list)

    val db = HockeyOpenHelper.getInstance(this).teamQueries
    val adapter = Adapter(db.selectAll().executeAsList()) { teamClicked ->
      val intent = Intent(this@TeamsActivity, PlayersActivity::class.java)
      intent.putExtra(PlayersActivity.TEAM_ID, teamClicked.id)
      startActivity(intent)
    }
    teams.layoutManager = LinearLayoutManager(this)
    teams.adapter = adapter
  }

  private inner class Adapter(
    private val data: List<Team>,
    private val clickListener: (Team) -> Unit
  ) : RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(layoutInflater.inflate(R.layout.team_row, parent, false) as TeamRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.setTeam(data[position])
    }

    inner class ViewHolder(val row: TeamRow) : RecyclerView.ViewHolder(row) {
      fun setTeam(team: Team) {
        row.populate(team)
        row.setOnClickListener { clickListener(team) }
      }
    }
  }
}
